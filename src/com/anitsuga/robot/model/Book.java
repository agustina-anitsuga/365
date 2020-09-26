package com.anitsuga.robot.model;

/**
 * Book
 * @author agustina.dagnino
 *
 */
public class Book extends Product {

    private String title;
    private String author;
    private String editorial;
    private String format;
    private String cover;
    private String coverFullData;
    private String genre;
    private String language;
    
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
    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    
}
