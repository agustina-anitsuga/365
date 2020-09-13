package com.anitsuga.invoicer.model;

/**
 * Sale
 * @author agustina
 *
 */
public class Sale {

    private String id;

    private String title;
    
    private String result;
    
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getSaleUrl(){
        String saleUrl = "https://www.mercadolibre.com.ar/ventas/"+this.getId()+"/detalle";
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
    
    
}
