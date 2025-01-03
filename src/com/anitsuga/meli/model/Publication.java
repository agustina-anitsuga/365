package com.anitsuga.meli.model;

/**
 * Publication
 * @author agustina
 *
 */
public class Publication {

    private String id;
    
    private String title;
    
    private Number price;
    
    private String isbn;
    
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Number getPrice() {
        return price;
    }

    public void setPrice(Number price) {
        this.price = price;
    }
    
    public String getPriceAsString(){
        String newPrice = getPrice().toString();
        Double doubleValue = Double.valueOf(newPrice);
        Long longValue = Math.round(doubleValue);
        return longValue.toString();
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
   
    
}
