package com.anitsuga.tools.model;

/**
 * ImageResult
 * @author agustina
 *
 */
public class ImageResult implements Result {
    
    private String isbn;
    private String images;
    
    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public String getImages() {
        return images;
    }
    public void setImages(String images) {
        this.images = images;
    }
    
}
