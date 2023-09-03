package com.anitsuga.invoicer;

import com.anitsuga.fwk.page.LoginPage;
import com.anitsuga.fwk.utils.Browser;
import com.anitsuga.fwk.utils.SeleniumUtils;
import com.anitsuga.invoicer.model.Sale;
import com.anitsuga.invoicer.page.SalePage;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * WebInvoiceStatusChecker
 */
public class WebInvoiceStatusChecker extends InvoiceStatusChecker {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebInvoiceStatusChecker.class.getName());


    private WebDriver driverMeli = null;

    /**
     * Input scanner
     */
    private Scanner scanner;


    protected void initialize() {
        driverMeli = getWebDriver();
    }

    /**
     * getWebDriver
     * @return
     */
    private WebDriver getWebDriver() {
        scanner = new Scanner(System.in);
        WebDriver driverMeli = SeleniumUtils.buildDriver(Browser.CHROME);
        this.login(driverMeli,"https://www.mercadolibre.com.ar/");
        this.promptForInput("Please log in and press ok");
        return driverMeli;
    }

    /**
     * getSaleStatus
     * @param sale
     * @return
     */
    protected String getSaleStatus(Sale sale) {
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
            ret = e.getMessage() + "";
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
