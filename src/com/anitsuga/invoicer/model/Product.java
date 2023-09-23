package com.anitsuga.invoicer.model;

/**
 * Product
 * @author agustina
 *
 */
public class Product {
    
    private String title;
    private String price;
    private int quantity;
    
    public Product( String title, String price, int quantity ){
        this.title = title;
        this.price = price;
        this.quantity = quantity;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append(this.getTitle());
        sb.append('\n');
        sb.append("Quantity:");
        sb.append(this.getQuantity());
        sb.append('\n');
        sb.append("Price:");
        sb.append(this.getFormattedPrice());
        return sb.toString();
    }

}
