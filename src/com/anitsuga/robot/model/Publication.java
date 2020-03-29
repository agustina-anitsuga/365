package com.anitsuga.robot.model;

import java.util.List;

/**
 * Publication
 * @author agustina.dagnino
 *
 */
public class Publication {

    private String url ;
    private String title ;
    private String price ;
    private List<String> images ;
    private String sku ;
    private int quantity ;
    private String description ;
    private String condition ;
    private Product product ;
    
    public List<String> getImages() {
        return images;
    }
    public void setImages(List<String> images) {
        this.images = images;
    }
    public String getSku() {
        return sku;
    }
    public void setSku(String sku) {
        this.sku = sku;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getCondition() {
        return condition;
    }
    public void setCondition(String condition) {
        this.condition = condition;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
    
    public String toString(){
        return "" + getTitle() + " [" + getPrice() + "] - "+getUrl();
    }
    
}
