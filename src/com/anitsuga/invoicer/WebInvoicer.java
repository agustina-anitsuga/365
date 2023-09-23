package com.anitsuga.invoicer;

import com.anitsuga.fwk.utils.Browser;
import com.anitsuga.fwk.utils.SeleniumUtils;
import com.anitsuga.invoicer.model.Customer;
import com.anitsuga.invoicer.model.InvoiceData;
import com.anitsuga.invoicer.model.Sale;
import com.anitsuga.invoicer.page.SalePage;
import org.openqa.selenium.WebDriver;

/**
 * WebInvoicer
 */
public class WebInvoicer  extends Invoicer {

    private WebDriver driverMeli;

    private SalePage salePage;

    protected void initializeSaleData(Sale sale) {
        String saleUrl = sale.getSaleUrl();
        SalePage salePage = new SalePage(driverMeli).go(saleUrl);
    }

    protected InvoiceData getInvoiceData() {
        return this.getInvoiceDataFrom(salePage);
    }

    protected boolean markAsInvoiced() {
        salePage.addNote("F");
        return true;
    }

    protected boolean saleIsAlreadyInvoiced() throws Exception {
        return salePage.includesInvoicedComment();
    }

    protected void initializeMeliConnection() {
        String url = "https://www.mercadolibre.com.ar/";
        WebDriver driverMeli = SeleniumUtils.buildDriver(Browser.CHROME);
        this.login(driverMeli, url);
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
}
