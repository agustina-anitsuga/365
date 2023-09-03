package com.anitsuga.invoicer;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import com.anitsuga.fwk.utils.*;
import com.anitsuga.invoicer.api.MeliRestClient;
import com.anitsuga.invoicer.api.Note;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anitsuga.fwk.page.LoginPage;
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
     * API_ENABLED
     */
    protected static final String API_ENABLED = "api.enabled";


    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceStatusChecker.class.getName());
    

    /**
     * main
     * @param args
     */
    public static void main(String[] args) {
        boolean isApiEnabled = AppProperties.getInstance().getBooleanProperty(API_ENABLED);
        InvoiceStatusChecker invoicer = null;
        if( isApiEnabled ) {
            invoicer = new ApiInvoiceStatusChecker();
        } else {
            invoicer = new WebInvoiceStatusChecker();
        }
        invoicer.run();
    }

    /**
     * run
     */
    public void run(){

        List<Sale> sales = getSales();
        initialize();

        int count = 0;
        int total = sales.size();
        for (Sale sale : sales) {
            System.out.println("Checking sale "+sale.getId()+ " ["+(++count)+"/"+total+"]");
            String result = getSaleStatus(sale);
            sale.setResult(result);
            System.out.println(result);   
            System.out.println("---");
            sleep();
        }
        
        writeResults(sales);
    }

    protected void initialize() {

    }

    protected String getSaleStatus(Sale sale) {
        return "";
    }

    /**
     * sleep
     */
    protected void sleep() {
        try {
            Thread.sleep(200);
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
     * getSalesToInvoice
     * @return
     */
    private List<Sale> getSales() {
        String filename = AppProperties.getInstance().getProperty(SALES_TO_INVOICE_FILE);
        InputDataReader reader = new InputDataReader(); 
        return reader.read(filename);
    }

}
