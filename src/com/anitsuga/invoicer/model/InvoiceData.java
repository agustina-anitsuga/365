package com.anitsuga.invoicer.model;

/**
 * InvoiceData
 * @author agustina
 *
 */
public class InvoiceData {

    private Product product;
    
    private Customer customer;

    
    public InvoiceData( Customer customer, Product product ){
        this.customer = customer;
        this.product = product;
    }
    
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
}
