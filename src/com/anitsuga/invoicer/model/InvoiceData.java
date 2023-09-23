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

    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append('\n');
        sb.append(customer.toString());
        sb.append('\n');
        sb.append('\n');
        for (Product product: products) {
            sb.append(product.toString());
            sb.append('\n');
            sb.append('\n');
        }
        return sb.toString();
    }
    
}
