package com.anitsuga.robot.model;

import java.util.List;

import com.anitsuga.utils.StringUtils;

/**
 * Book
 * @author agustina.dagnino
 *
 */
public class Book extends Product {

    private String isbn;
    private String title;
    private String author;
    private String editorial;
    private String format;
    private String cover;
    private String coverFullData;
    private String price;
    private String weight;
    private String availability;
    private String language;
    private String type;
    private String dimensions;
    private List<String> images ;
    private String seller;
    
    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getEditorial() {
        return editorial;
    }
    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }
    public String getFormat() {
        return format;
    }
    public void setFormat(String format) {
        this.format = format;
    }
    public String getCover() {
        return cover;
    }
    public void setCover(String cover) {
        this.cover = cover;
    }
    public String getCoverFullData() {
        return coverFullData;
    }
    public void setCoverFullData(String coverFullData) {
        this.coverFullData = coverFullData;
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
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
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
        String weightStr = this.getWeight();
        if( weightStr.contains("pounds") ){
            weightStr = weightStr.replaceAll("pounds", "");
            double poundToKilo = 0.454;
            Number weight = StringUtils.parse(weightStr.trim());
            ret = Double.valueOf(poundToKilo * weight.doubleValue());
        } else if ( weightStr.contains("ounces") ){
            weightStr = weightStr.replaceAll("ounces", "");
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
        return StringUtils.parse(dolarStr.trim());
    }
   
}
