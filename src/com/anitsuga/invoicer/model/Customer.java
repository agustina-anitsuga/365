package com.anitsuga.invoicer.model;

/**
 * Customer
 * @author agustina
 *
 */
public class Customer {

    private String docType;
    private String docNumber;
    private String address;
    private String name;

    private String customerType;
    
    public Customer( String docType, String docNumber, String address, String name, String customerType ){
        this.docType = docType ;
        this.docNumber = docNumber ;
        this.address = address;
        this.name = name;
        this.customerType = customerType;
    }
    
    public String getDocType() {
        return docType;
    }
    public void setDocType(String docType) {
        this.docType = docType;
    }
    public String getDocNumber() {
        return docNumber;
    }
    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCustomerType(){
        return this.customerType;
    }

    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("Customer: ");
        sb.append(this.getDocType());
        sb.append(' ');
        sb.append(this.getDocNumber());
        sb.append(' ');
        sb.append(this.getName());
        sb.append('\n');
        sb.append(this.getAddress());
        sb.append('\n');
        sb.append(this.getCustomerType());
        return sb.toString();
    }
}
