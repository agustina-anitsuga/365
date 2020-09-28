package com.anitsuga.invoicer.model;

import java.util.ArrayList;
import java.util.List;

/**
 * InvoiceData
 * @author agustina
 *
 */
public class InvoiceData {

    private List<Product> products;
    
    private Customer customer;

    
    public InvoiceData( Customer customer, Product product ){
        this.customer = customer;
        this.products = new ArrayList<Product>();
        this.products.add(product);
    }

    public InvoiceData( Customer customer, List<Product> products ){
        this.customer = customer;
        this.products = products;
    }
    
    public List<Product> getProducts(){
        return products;
    }
    
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
}
