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
    
    public Customer( String docType, String docNumber, String address, String name ){
        this.docType = docType ;
        this.docNumber = docNumber ;
        this.address = address;
        this.name = name;
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
    
}
