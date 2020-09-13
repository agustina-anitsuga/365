package com.anitsuga.invoicer.model;

/**
 * Sale
 * @author agustina
 *
 */
public class Sale {

    private String id;

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
}
