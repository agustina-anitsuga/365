package com.anitsuga.robot.model;

/**
 * SellerQuote
 * @author agustina
 *
 */
public class SellerQuote {

    
    private String seller;
    private String distributor;
    private String price;
    
    
    public String getSeller() {
        return seller;
    }
    public void setSeller(String seller) {
        this.seller = seller;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getDistributor() {
        return distributor;
    }
    public void setDistributor(String distributor) {
        this.distributor = distributor;
    }
    
}
