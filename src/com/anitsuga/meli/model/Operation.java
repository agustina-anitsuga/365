package com.anitsuga.meli.model;

/**
 * Operation
 * @author agustina
 *
 */
public class Operation {

    private Publication publication;
    
    private String error;

    
    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    
    
}
