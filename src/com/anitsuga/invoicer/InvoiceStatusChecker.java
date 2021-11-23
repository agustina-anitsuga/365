package com.anitsuga.invoicer;

import java.util.List;
import java.util.Scanner;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anitsuga.fwk.page.LoginPage;
import com.anitsuga.fwk.utils.AppProperties;
import com.anitsuga.fwk.utils.Browser;
import com.anitsuga.fwk.utils.FileUtils;
import com.anitsuga.fwk.utils.SeleniumUtils;
import com.anitsuga.invoicer.model.Sale;
import com.anitsuga.invoicer.page.SalePage;
import com.anitsuga.invoicer.reader.InputDataReader;
import com.anitsuga.invoicer.writer.ResultExcelWriter;

/**
 * InvoiceStatusChecker
 * @author agustina
 *
 */
public class InvoiceStatusChecker {

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
        InvoiceStatusChecker invoicer = new InvoiceStatusChecker();
        invoicer.run();
    }

    /**
     * run
     */
    public void run(){
        
        List<Sale> sales = getSales();
        
        scanner = new Scanner(System.in);
        
        WebDriver driverMeli = SeleniumUtils.buildDriver(Browser.CHROME);
        this.login(driverMeli,"https://www.mercadolibre.com.ar/");
        this.promptForInput("Please log in and press ok");
        
        int count = 0;
        int total = sales.size();
        for (Sale sale : sales) {
            System.out.println("Checking sale "+sale.getId()+ " ["+(++count)+"/"+total+"]");
            String result = getSaleStatus(driverMeli, sale);
            sale.setResult(result);
            System.out.println(result);   
            System.out.println("---");
            sleep();
        }
        
        writeResults(sales);
    }

    /**
     * sleep
     */
    private void sleep() {
        try {
            Thread.sleep(500);
        } catch (Exception e) {
            // nothing
        }
    }

    /**
     * writeResults
     */
    private void writeResults(List<Sale> sales) {
        String localPath = FileUtils.getLocalPath();
        String filename = localPath + outputFilePrefix();
        ResultExcelWriter writer = new ResultExcelWriter(); 
        writer.write(filename, sales);
        System.out.println("Finished sale checking job");
    }

    /**
     * outputFilePrefix
     * @return
     */
    private String outputFilePrefix() {
        return "sales-status";
    }

    /**
     * invoice
     * @param driverMeli
     * @param sale
     * @return
     */
    private String getSaleStatus(WebDriver driverMeli, Sale sale) {
        String ret = "";
        try {
            
            String saleUrl = sale.getSaleUrl();
            SalePage salePage = new SalePage(driverMeli).go(saleUrl);
            if ( salePage.includesInvoicedComment() ){
                ret = "Already invoiced";
            } else {
                ret = "Invoice Pending";
            }
            
        } catch (Exception e) {
            ret = e.getMessage();
        }
        return ret;
    }

    /**
     * getSalesToInvoice
     * @return
     */
    private List<Sale> getSales() {
        String filename = AppProperties.getInstance().getProperty(SALES_TO_INVOICE_FILE);
        InputDataReader reader = new InputDataReader(); 
        return reader.read(filename);
    }
    
    /**
     * login
     */
    protected void login( WebDriver driver, String url ){   
        LoginPage page = new LoginPage(driver);
        page.go(url);
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
