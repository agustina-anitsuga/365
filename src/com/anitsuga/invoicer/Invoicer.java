package com.anitsuga.invoicer;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anitsuga.fwk.page.LoginPage;
import com.anitsuga.fwk.utils.AppProperties;
import com.anitsuga.fwk.utils.Browser;
import com.anitsuga.fwk.utils.FileUtils;
import com.anitsuga.fwk.utils.SeleniumUtils;
import com.anitsuga.invoicer.model.Customer;
import com.anitsuga.invoicer.model.InvoiceData;
import com.anitsuga.invoicer.model.Product;
import com.anitsuga.invoicer.model.Sale;
import com.anitsuga.invoicer.page.InvoiceDetailPage;
import com.anitsuga.invoicer.page.InvoiceHeaderPage;
import com.anitsuga.invoicer.page.InvoiceTypePage;
import com.anitsuga.invoicer.page.MenuPage;
import com.anitsuga.invoicer.page.ProductTypePage;
import com.anitsuga.invoicer.page.SalePage;
import com.anitsuga.invoicer.reader.InputDataReader;
import com.anitsuga.invoicer.writer.ResultExcelWriter;

/**
 * Invoicer
 * @author agustina
 *
 */
public class Invoicer {

    /**
     * SALES_TO_INVOICE_FILE
     */
    protected static final String SALES_TO_INVOICE_FILE = "sales.to.invoice.file";
    
    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceStatusChecker.class.getName());
    
    /**
     * Input scanner
     */
    private Scanner scanner;
    
    
    /**
     * main
     * @param args
     */
    public static void main(String[] args) {
        Invoicer invoicer = new Invoicer();
        invoicer.run();
    }

    /**
     * run
     */
    public void run(){
        
        List<Sale> sales = getSalesToInvoice();
        
        scanner = new Scanner(System.in);
        
        WebDriver driverMeli = SeleniumUtils.buildDriver(Browser.CHROME);
        this.login(driverMeli,"https://www.mercadolibre.com.ar/");
        
        WebDriver driverInv = SeleniumUtils.buildDriver(Browser.CHROME);
        this.login(driverInv, "http://www.afip.gob.ar/sitio/externos/default.asp");
        
        initializeProcess(driverInv);
        
        int count = 0;
        int total = sales.size();
        for (Sale sale : sales) {
            System.out.println("Invoicing sale "+sale.getId()+ " ["+(++count)+"/"+total+"]");
            String result = invoice(driverMeli, driverInv, sale);
            sale.setResult(result);
            System.out.println(result);   
            System.out.println("---");
            String input = this.promptForInput("Proceed to next invoice? (yes/no):");
            if( "no".equals(input) ){
                break;
            }
        }
        
        writeResults(sales);
    }

    /**
     * writeResults
     */
    private void writeResults(List<Sale> sales) {
        String localPath = FileUtils.getLocalPath();
        String filename = localPath + outputFilePrefix();
        ResultExcelWriter writer = new ResultExcelWriter(); 
        writer.write(filename, sales);
        System.out.println("Finished invoicing job");
    }

    /**
     * outputFilePrefix
     * @return
     */
    private String outputFilePrefix() {
        return "invoiced-sales";
    }

    /**
     * invoice
     * @param driverMeli
     * @param driverInv
     * @param sale
     * @return
     */
    private String invoice(WebDriver driverMeli, WebDriver driverInv, Sale sale) {
        String ret = "";
        try {
            
            String saleUrl = sale.getSaleUrl();
            SalePage salePage = new SalePage(driverMeli).go(saleUrl);
            if ( salePage.includesInvoicedComment() ){
                ret = "Already invoiced";
            } else {
                InvoiceData invoiceData = this.getInvoiceDataFrom(salePage);
                if( invoiceData==null ){
                    ret = "No data invoice";
                } else {
                    
                    String title = invoiceData.getProducts().get(0).getTitle();
                    sale.setTitle(title);
                    System.out.println("    "+title);
                    
                    this.executeInvoiceWorkflow(driverInv, invoiceData);
                    
                    String input = this.promptForInput("Mark sale as invoiced? (yes/no): ");
                    if( "yes".equals(input) )
                    {
                        salePage.addNote("F");
                        ret = "Marked as invoiced";
                    } else {
                        ret = "Not marked as invoiced ["+input+"]";
                    }
                }
            }
            
        } catch (Exception e) {
            ret = e.getMessage();
        }
        return ret;
    }

    /**
     * executeInvoiceWorkflow
     * @param driverInv
     * @param invoiceData
     * @return
     */
    private void executeInvoiceWorkflow(WebDriver driverInv, InvoiceData invoiceData) {
        
        MenuPage menuPage = new MenuPage(driverInv);
        menuPage.go("https://fe.afip.gob.ar/rcel/jsp/menu_ppal.jsp");
        menuPage.clickGenerateInvoice();
        
        doWorkflowStep1InvoiceType(driverInv);
        doWorkflowStep2ProductType(driverInv);
        doWorkflowStep3InvoiceHeader(driverInv, invoiceData);
        doWorkflowStep4InvoiceDetails(driverInv, invoiceData);
        
        /*
        String input = this.promptForInput("Generate and download invoice automatically? (yes/no):");
        if( "yes".equals(input) ){
            InvoiceSummaryPage summary = new InvoiceSummaryPage(driverInv);
            summary.confirm();
        }
        */
    }

    /**
     * doWorkflowStep4InvoiceDetails
     * @param driverInv
     * @param invoiceData
     */
    private void doWorkflowStep4InvoiceDetails(WebDriver driverInv, InvoiceData invoiceData) {
        try {
            InvoiceDetailPage invoiceDetail = new InvoiceDetailPage(driverInv); 
            List<Product> products = invoiceData.getProducts();
            int line = 1;
            for (Product product : products) {
                invoiceDetail.setProduct( line, product.getTitle() );
                invoiceDetail.setPrice( line, product.getFormattedPrice() );
                invoiceDetail.setIva( line, product.getIVA() );   
                invoiceDetail.setQuantity( line, product.getQuantity() );
                line = line + 1;
                if(line<=products.size()){
                    invoiceDetail.newLine();
                    doWait(500);
                }
        }
        invoiceDetail.clickNext();
        } catch( Exception e ){
            e.printStackTrace();
            this.promptForInput("Could you fix the error, please? (and go to the next step)");
        }
    }

    /**
     * doWorkflowStep3InvoiceHeader
     * @param driverInv
     * @param invoiceData
     */
    private void doWorkflowStep3InvoiceHeader(WebDriver driverInv, InvoiceData invoiceData) {
        try {
            InvoiceHeaderPage invoiceHeader = new InvoiceHeaderPage(driverInv); 
            invoiceHeader.setDefaultCustomerType();
            doWait(500);
            invoiceHeader.setCustomerDocType( invoiceData.getCustomer().getDocType() );
            invoiceHeader.setCustomerDocNumber( invoiceData.getCustomer().getDocNumber() );
            doWait(500);
            invoiceHeader.setCustomerAddress( invoiceData.getCustomer().getAddress() );
            invoiceHeader.setDefaultPaymentType();
            
            validateCustomerName(invoiceData, invoiceHeader);
            
            invoiceHeader.clickNext();
        } catch( Exception e ) {
            e.printStackTrace();
            this.promptForInput("Could you fix the error, please? (and go to the next step)");
        }
    }

    /**
     * validateCustomerName
     * @param invoiceData
     * @param invoiceHeader
     */
    private void validateCustomerName(InvoiceData invoiceData, InvoiceHeaderPage invoiceHeader) {
        String nameInSalesSystem = invoiceData.getCustomer().getName();
        String[] nameParts = nameInSalesSystem.split(" ");
        String nameInInvoicingSystem = sanitize(invoiceHeader.getCustomerName());
        boolean anyMatch = false;
        for (String namePart : nameParts) {
            anyMatch |= nameInInvoicingSystem.contains(sanitize(namePart));
        }
        if(!anyMatch)
            this.promptForInput("Could you check the customer name, please? (input any string when done)");
    }

    /**
     * sanitize
     * @param string
     * @return
     */
    private String sanitize(String string) {
        return string.toUpperCase();
    }

    /**
     * doWorkflowStep2ProductType
     * @param driverInv
     */
    private void doWorkflowStep2ProductType(WebDriver driverInv) {
        ProductTypePage productType = new ProductTypePage(driverInv);     
        productType.selectDefaultProductType();
        productType.clickNext();
    }

    /**
     * doWorkflowStep1InvoiceType
     * @param driverInv
     */
    private void doWorkflowStep1InvoiceType(WebDriver driverInv) {
        InvoiceTypePage invoiceType = new InvoiceTypePage(driverInv);
        invoiceType.selectDefaultSalesPoint();
        doWait(500);
        invoiceType.selecteDefaultInvoiceType();
        invoiceType.clickNext();
    }
    
    /**
     * doWait
     * @param millis
     */
    private void doWait(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            // nothing
        }
    }

    /**
     * getSalesToInvoice
     * @return
     */
    private List<Sale> getSalesToInvoice() {
        String filename = AppProperties.getInstance().getProperty(SALES_TO_INVOICE_FILE);
        InputDataReader reader = new InputDataReader(); 
        return reader.read(filename);
    }

    /**
     * getInvoiceDataFrom
     * @param salePage
     * @return
     */
    private InvoiceData getInvoiceDataFrom(SalePage salePage) {
        InvoiceData ret = null;
        if( !salePage.includesInvoicedComment() ){
            Customer customer = new Customer(
                        salePage.getCustomerDocType(),
                        salePage.getCustomerDocNumber(),
                        salePage.getCustomerAddress(),
                        salePage.getCustomerName()
                        );
            ret = new InvoiceData(customer,salePage.getProducts());
        }
        return ret;
    }
    
    /**
     * login
     */
    protected void login( WebDriver driver, String url ){   
        LoginPage page = new LoginPage(driver);
        page.go(url);
    }

    /**
     * initializeProcess
     * @param driver
     */
    private void initializeProcess(WebDriver driver) {
        promptForInput("Enter any message once you have logged in to both Meli and Afip ");
        String lastTab = "";
        Set<String> tabsAfter = driver.getWindowHandles();
        for (String tabName : tabsAfter) {
            lastTab = tabName;
        }
        driver.switchTo().window(lastTab);
        try {
            WebElement company = driver.findElement( By.xpath("//*[@id=\"contenido\"]/form/table/tbody/tr[4]/td/input[2]"));
        company.click();
        } catch( Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * promptForInput
     */
    protected String promptForInput(String prompt) {
        String ret = null;
        try {
            System.out.println(prompt);
            ret = scanner.nextLine();
            System.out. println("You entered string "+ret);
        } catch (Exception e){
            LOGGER.error(e.getMessage());
        } 
        return ret;
    }
    
}
