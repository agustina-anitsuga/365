package com.anitsuga.invoicer;

import com.anitsuga.fwk.utils.Browser;
import com.anitsuga.fwk.utils.SeleniumUtils;
import com.anitsuga.invoicer.api.*;
import com.anitsuga.invoicer.model.Customer;
import com.anitsuga.invoicer.model.InvoiceData;
import com.anitsuga.invoicer.model.Product;
import com.anitsuga.invoicer.model.Sale;
import com.anitsuga.invoicer.page.SalePage;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ApiInvoicer
 */
public class ApiInvoicer extends Invoicer {

    private MeliRestClient client = new MeliRestClient();

    private Sale currentSale = null;

    protected Sale getCurrentSale(){
        return this.currentSale;
    }

    protected void initializeMeliConnection() {
        // do nothing
    }

    protected boolean saleIsAlreadyInvoiced() {
        boolean ret = false;
        List<Note> notes = client.getNotes(this.getCurrentSale().getId());
        if (notes != null) {
            for (Note note : notes) {
                if (note.getNote().contains("F")) {
                    ret = true;
                    break;
                }
            }
        }
        return ret;
    }

    protected void initializeSaleData(Sale sale) {
        this.currentSale = sale;
    }

    protected InvoiceData getInvoiceData() {

        Customer customer = getCustomer();
        List<Product> products = getProducts();
        InvoiceData ret = new InvoiceData( customer, products );

        return ret;
    }

    private List<Product> getProducts() {
        List<Product> products = new ArrayList<Product>();
        List<OrderItem> items = client.getOrderDetails(this.getCurrentSale().getId());
        for (OrderItem item: items) {
            Product product = new Product( item.getItem().getTitle(), item.getUnit_price(), item.getQuantity());
            products.add(product);
        }
        return products;
    }

    private Customer getCustomer() {
        BillingInfo billingInfo = client.getBillingInfo(this.getCurrentSale().getId());
        String docType = billingInfo.getDoc_type();
        String docNumber = billingInfo.getDoc_number();
        Map<String, String> info = map(billingInfo.getAdditional_info());
        String address = getAddress(info);
        String name = getName(info);;
        Customer customer = new Customer( docType,  docNumber,  address,  name);
        return customer;
    }

    private Map<String, String> map(List<AdditionalInfo> additional_info) {
        Map<String, String> ret = new HashMap<String, String>();
        for (AdditionalInfo info: additional_info ) {
            ret.put(info.getType(),info.getValue());
        }
        return ret;
    }

    private String getAddress( Map<String,String> info ){
        return info.get("STREET_NAME") + " " + info.get("STREET_NUMBER") + ", " + info.get("CITY_NAME") + ", " + info.get("STATE_NAME") ;
    }

    private String getName( Map<String,String> info ){
        return info.get("FIRST_NAME") + " " + info.get("LAST_NAME");
    }

    protected void markAsInvoiced() {
        client.addNote(this.getCurrentSale().getId(),"F");
    }
}
