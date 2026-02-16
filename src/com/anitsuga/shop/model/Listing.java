package com.anitsuga.shop.model;

import java.math.BigInteger;

/**
 * Product
 * @author agustina
 *
 */
public class Listing {

    private String meliId;

    private String result;
    
    private long duration;


    public String getMeliId() {
        return meliId;
    }

    public void setMeliId(String meliId) {
        this.meliId = meliId;
    }

    public String geProductUrl(){
        String meliId = this.getMeliId().replaceFirst("([A-Z]{3})", "$1-");
        String saleUrl = "https://articulo.mercadolibre.com.ar/"+meliId;
        return saleUrl;
    }

    public String getNubeId() {
        return getResultIfNumeric();
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

    private String getResultIfNumeric() {
        return isBigInteger(this.getResult())? this.getResult() : "";
    }

    private boolean isBigInteger(String str) {
        if (str == null || str.isBlank()) {
            return false;
        }
        try {
            new BigInteger(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
