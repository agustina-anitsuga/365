package com.anitsuga.tools.model;

/**
 * UPCResult
 * @author agustina
 *
 */
public class UPCResult implements Result {

    private String asin;
    private String upc;
    private String releaseYear;
    
    public String getAsin() {
        return asin;
    }
    public void setAsin(String asin) {
        this.asin = asin;
    }
    public String getUpc() {
        return upc;
    }
    public void setUpc(String upc) {
        this.upc = upc;
    }
    public String getReleaseYear() {
        return releaseYear;
    }
    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }
      
}
