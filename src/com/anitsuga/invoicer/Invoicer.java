package com.anitsuga.invoicer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anitsuga.fwk.page.LoginPage;
import com.anitsuga.fwk.utils.Browser;
import com.anitsuga.fwk.utils.SeleniumUtils;
import com.anitsuga.invoicer.model.Customer;
import com.anitsuga.invoicer.model.InvoiceData;
import com.anitsuga.invoicer.model.Product;
import com.anitsuga.invoicer.model.Sale;
import com.anitsuga.invoicer.page.InvoiceDetailPage;
import com.anitsuga.invoicer.page.InvoiceHeaderPage;
import com.anitsuga.invoicer.page.InvoiceSummaryPage;
import com.anitsuga.invoicer.page.InvoiceTypePage;
import com.anitsuga.invoicer.page.MenuPage;
import com.anitsuga.invoicer.page.ProductTypePage;
import com.anitsuga.invoicer.page.SalePage;

/**
 * Invoicer
 * @author agustina
 *
 */
public class Invoicer {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Invoicer.class.getName());
    
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
        this.loginMeli(driverMeli);
        
        WebDriver driverInv = SeleniumUtils.buildDriver(Browser.CHROME);
        this.loginInv(driverInv);
        
        initializeProcess(driverInv);
        
        int count = 0;
        int total = sales.size();
        for (Sale sale : sales) {
            System.out.println("Invoicing sale "+sale.getId()+ " ["+(++count)+"/"+total+"]");
            String result = invoice(driverMeli, driverInv, sale);
            System.out.println(result);   
            System.out.println("---");
        }
        
        writeResults();
    }

    /**
     * writeResults
     */
    private void writeResults() {
        // TODO Auto-generated method stub
        
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
            InvoiceData invoiceData = this.getInvoiceDataFrom(salePage);
            
            ret = executeInvoiceWorkflow(driverInv, invoiceData);
            
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
    private String executeInvoiceWorkflow(WebDriver driverInv, InvoiceData invoiceData) {
        
        MenuPage menuPage = new MenuPage(driverInv);
        menuPage.go("https://serviciosjava2.afip.gob.ar/rcel/jsp/menu_ppal.jsp");
        menuPage.clickGenerateInvoice();
        
        InvoiceTypePage invoiceType = new InvoiceTypePage(driverInv);
        invoiceType.selectDefaultSalesPoint();
        doWait(1000);
        invoiceType.selecteDefaultInvoiceType();
        invoiceType.clickNext();
        
        ProductTypePage productType = new ProductTypePage(driverInv);     
        productType.selectDefaultProductType();
        productType.clickNext();
        
        InvoiceHeaderPage invoiceHeader = new InvoiceHeaderPage(driverInv); 
        invoiceHeader.setDefaultCustomerType();
        doWait(1000);
        invoiceHeader.setCustomerDocType( invoiceData.getCustomer().getDocType() );
        invoiceHeader.setCustomerDocNumber( invoiceData.getCustomer().getDocNumber() );
        doWait(1000);
        invoiceHeader.setCustomerAddress( invoiceData.getCustomer().getAddress() );
        invoiceHeader.setDefaultPaymentType();
        invoiceHeader.clickNext();
        
        InvoiceDetailPage invoiceDetail = new InvoiceDetailPage(driverInv); 
        invoiceDetail.setProduct( invoiceData.getProduct().getTitle() );
        invoiceDetail.setPrice( invoiceData.getProduct().getFormattedPrice() );
        invoiceDetail.setIva( invoiceData.getProduct().getIVA() );
        invoiceDetail.clickNext();
        
        InvoiceSummaryPage summary = new InvoiceSummaryPage(driverInv);
        summary.confirm();
        
        System.out.println("Enter any message in order to proceed with the next invoice");
        this.waitForInput();
        
        return "ok";
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
        List<Sale> sales = new ArrayList<Sale>();
        Sale sale = new Sale();
        sale.setId("4034208473");
        sales.add(sale);
        sale = new Sale();
        sale.setId("4027361250");
        sales.add(sale);
        sale = new Sale();
        sale.setId("4033249859");
        sales.add(sale);
        return sales;
    }

    /**
     * getInvoiceDataFrom
     * @param salePage
     * @return
     */
    private InvoiceData getInvoiceDataFrom(SalePage salePage) {
        Product product = new Product(salePage.getProductTitle(),salePage.getProductPrice());
        Customer customer = new Customer(salePage.getCustomerDocType(),salePage.getCustomerDocNumber(),salePage.getCustomerAddress());
        return new InvoiceData(customer,product);
    }
    
    /**
     * loginMeli
     */
    protected void loginMeli( WebDriver driver ){
        String url = "https://www.mercadolibre.com.ar/";
        LoginPage page = new LoginPage(driver);
        page.go(url);
    }

    /**
     * loginInv
     */
    protected void loginInv( WebDriver driver ){
        String url = "http://www.afip.gob.ar/sitio/externos/default.asp";
        LoginPage page = new LoginPage(driver);
        page.go(url);
    }

    /**
     * initializeProcess
     * @param driver
     */
    private void initializeProcess(WebDriver driver) {
        System.out.println("Enter a message once you have logged in to both Meli and Afip");
        waitForInput();
        String lastTab = "";
        Set<String> tabsAfter = driver.getWindowHandles();
        for (String tabName : tabsAfter) {
            lastTab = tabName;
        }
        driver.switchTo().window(lastTab);
        WebElement company = driver.findElement( By.xpath("//*[@id=\"contenido\"]/form/table/tbody/tr[4]/td/input[2]"));
        company.click();
    }

    /**
     * waitForInput
     */
    protected void waitForInput() {
        try {
            String s = scanner.nextLine();
            System.out. println("You entered string "+s);
        } catch (Exception e){
            LOGGER.error(e.getMessage());
        } 
    }
    
}
