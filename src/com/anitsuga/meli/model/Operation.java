package com.anitsuga.meli.model;

/**
 * Operation
 * @author agustina
 *
 */
public class Operation {

    private Publication publication;
    
    private String result;

    
    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String error) {
        this.result = error;
    }
    
    
}
