package com.anitsuga.shop.model;

/**
 * Product
 * @author agustina
 *
 */
public class Listing {

    private String id;

    private String title;
    
    private String result;
    
    private long duration;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String geProductUrl(){
        String saleUrl = "https://articulo.mercadolibre.com.ar/"+this.getId();
        return saleUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }


    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }
}
