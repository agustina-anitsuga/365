package com.anitsuga.robot.model;

import java.util.List;

import com.anitsuga.fwk.utils.StringUtils;

/**
 * Product
 * @author agustina.dagnino
 *
 */
public abstract class Product {

    private String price;
    private String weight;
    private String availability;
    private String dimensions;
    private List<String> images ;
    private String seller;
    private String genericIdentifier;
    

    public String getGenericIdentifier() {
        return genericIdentifier;
    }
    public void setGenericIdentifier(String isbn) {
        this.genericIdentifier = isbn;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getWeight() {
        return weight;
    }
    public void setWeight(String weight) {
        this.weight = weight;
    }
    public String getAvailability() {
        return availability;
    }
    public void setAvailability(String availability) {
        this.availability = availability;
    }
    public String getDimensions() {
        return dimensions;
    }
    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }
    public List<String> getImages() {
        return images;
    }
    public void setImages(List<String> images) {
        this.images = images;
    }
    public String getSeller() {
        return seller;
    }
    public void setSeller(String seller) {
        this.seller = seller;
    }
  
    
    public Number getWeightInKilos() {
        Double ret = null;
        String weightStr = this.getWeight().toLowerCase();
        if( weightStr.contains("pounds") || weightStr.contains("libras") ){
            weightStr = weightStr.replaceAll("pounds", "");
            weightStr = weightStr.replaceAll("libras", "");
            double poundToKilo = 0.454;
            Number weight = StringUtils.parse(weightStr.trim());
            ret = Double.valueOf(poundToKilo * weight.doubleValue());
        } else if ( weightStr.contains("ounces") || weightStr.contains("onzas") ){
            weightStr = weightStr.replaceAll("ounces", "");
            weightStr = weightStr.replaceAll("onzas", "");
            double ounceToKilo = 0.028;
            Number weight = StringUtils.parse(weightStr.trim());
            ret = Double.valueOf(ounceToKilo * weight.doubleValue());
        } 
        return ret;
    }
    
    public Number getDolarPriceAmount() {
        String dolarStr = this.getPrice();
        dolarStr = dolarStr.replaceAll(java.util.regex.Matcher.quoteReplacement("US$"), "");
        dolarStr = dolarStr.replaceAll(java.util.regex.Matcher.quoteReplacement("$"), "");
        dolarStr = dolarStr.replaceAll("USD", "");
        Number n =  StringUtils.parse(dolarStr.trim());
        return n;
    }
   
    
}
