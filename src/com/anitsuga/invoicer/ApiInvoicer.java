package com.anitsuga.invoicer;

import com.anitsuga.invoicer.api.*;
import com.anitsuga.invoicer.model.Customer;
import com.anitsuga.invoicer.model.InvoiceData;
import com.anitsuga.invoicer.model.Product;
import com.anitsuga.invoicer.model.Sale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ApiInvoicer
 */
public class ApiInvoicer extends Invoicer {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiInvoicer.class.getName());

    private MeliRestClient client = new MeliRestClient();

    private Sale currentSale = null;

    private Order currentOrder = null;


    protected Sale getCurrentSale(){
        return this.currentSale;
    }

    protected Order getCurrentOrder() { return this.currentOrder; }


    protected void initializeMeliConnection() {
        // do nothing
    }

    protected boolean saleIsAlreadyInvoiced() throws Exception {
        boolean ret = false;

        Order order = this.getCurrentOrder();

        if( order==null ){
            throw new Exception("Order could not be retrieved.");
        }
        if( !order.isFulfilled() ){
            throw new Exception("Order is not fulfilled yet. Should not be invoiced.");
        }
        if( !"paid".equals(order.getStatus()) ){
            throw new Exception("Order is not paid. Should not be invoiced.");
        }
        for( int i=0; i<order.getOrder_items().size(); i++) {
            order.getTags().remove("paid");
            order.getTags().remove("delivered");
            order.getTags().remove("pack_order");
            order.getTags().remove("catalog");
            order.getTags().remove("not_delivered");
            order.getTags().remove("3x_campaign");
            order.getTags().remove("pcj-co-funded");
            order.getTags().remove("new_buyer_free_shipping");
            order.getTags().remove("b2b");
            order.getTags().remove("order_has_discount");
            order.getTags().remove("no_shipping");
        }
        if( order.getTags().size()>0 ){
            StringBuffer sb = new StringBuffer("");
            for( String tag : order.getTags()){
                sb.append(tag);
                sb.append(" ");
            }
            LOGGER.info(sb.toString());
            throw new Exception("Order tags not recognized. Should not be invoiced.");
        }
        if( order.getCancel_details()!=null ){
            throw new Exception("Order has been cancelled. Should not be invoiced.");
        }
        if( order.getOrder_request().getChange()!=null || order.getOrder_request().getReturn()!=null ){
            throw new Exception("Order has a change or return. Should not be invoiced.");
        }
        if( hasUploadedInvoices(order) ){
            throw new Exception("Order has an invoice already uploaded. Do not invoice again. Please add invoiced mark.");
        }

        List<Note> notes = client.getNotes(this.getCurrentSale().getId());
        if (notes != null) {
            for (Note note : notes) {
                if (note.getNote().contains("F")) {
                    ret = true;
                    break;
                }
            }
        } else {
            throw new Exception("Cannot determine if sale is already invoiced");
        }
        return ret;
    }

    private boolean hasUploadedInvoices(Order order) {
        return client.isInvoiced(order.getId());
    }

    protected void initializeSaleData(Sale sale) {
        this.currentSale = sale;
        this.currentOrder = client.getOrder(this.getCurrentSale().getId());
    }

    protected InvoiceData getInvoiceData() {
        InvoiceData ret = null;
        try {
            Customer customer = getCustomer();
            List<Product> products = getProducts();
            ret = new InvoiceData(customer, products);
            System.out.println(ret.toString());
        } catch (Exception e){
            LOGGER.error(e.getMessage(),e.getStackTrace());
            ret = null;
        }
        return ret;
    }

    private List<Product> getProducts() {
        List<Product> products = new ArrayList<Product>();
        Order order = this.getCurrentOrder();
        List<OrderItem> items = order.getOrder_items();
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
        String name = getName(info);
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
        return strClean(info.get("STREET_NAME")) + " "
                + strClean(info.get("STREET_NUMBER")) + ", "
                + strClean(info.get("CITY_NAME")) + ", "
                + strClean(info.get("STATE_NAME")) ;
    }

    private String strClean(String street_name) {
        if(street_name==null||street_name.equals("null")){
            return "";
        }
        return street_name;
    }

    private String getName( Map<String,String> info ){
        String ret = null;
        if ( "CUIT".equals(info.get("DOC_TYPE")) ){
            ret = info.get("BUSINESS_NAME");
        } else {
            ret = info.get("FIRST_NAME") + " " + info.get("LAST_NAME");
        }
        return ret;
    }

    protected boolean markAsInvoiced() {
        Note note = client.addNote(this.getCurrentSale().getId(),"F");
        return note.getDate_created() != null;
    }
}
