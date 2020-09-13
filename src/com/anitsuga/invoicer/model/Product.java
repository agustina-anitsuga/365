package com.anitsuga.invoicer.model;

/**
 * Product
 * @author agustina
 *
 */
public class Product {
    
    private String title;
    private String price;
    
    public Product( String title, String price ){
        this.title = title;
        this.price = price;
    }
    
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    
    public String getFormattedPrice(){
        String aPrice = this.getPrice().replaceAll(",", ".");
        Double d = (new Double(aPrice));
        Long l = Math.round(d);
        return l.toString();
    }
    
    public String getIVA(){
        if( this.getTitle().startsWith("Libro") ){
            return "Exento";
        } else {
            return "21%";
        }
    }
}
