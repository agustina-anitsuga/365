package com.anitsuga.robot.model;

/**
 * Movie
 * @author agustina
 *
 */
public class Movie extends Product {
 
    private String format;
    private String title;
    private String director;
    private String videoResolution;
    private String numberOfDisks;
    private String audio;
    private String genre;
    private String studio;
    
    private String runningTime;
    private String releaseDate;
    private String cast;
    private String subtitles;
    private String producers;
    private String writers;
    
    
    public String getFormat() {
        return format;
    }
    public void setFormat(String format) {
        this.format = format;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDirector() {
        return director;
    }
    public void setDirector(String director) {
        this.director = director;
    }
    public String getVideoResolution() {
        return videoResolution;
    }
    public void setVideoResolution(String videoResolution) {
        this.videoResolution = videoResolution;
    }
    public String getNumberOfDisks() {
        return numberOfDisks;
    }
    public void setNumberOfDisks(String numberOfDisks) {
        this.numberOfDisks = numberOfDisks;
    }
    public String getAudio() {
        return audio;
    }
    public void setAudio(String audio) {
        this.audio = audio;
    }
    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public String getStudio() {
        return studio;
    }
    public void setStudio(String studio) {
        this.studio = studio;
    }
    
    
    public String getRunningTime() {
        return runningTime;
    }
    public void setRunningTime(String runningTime) {
        this.runningTime = runningTime;
    }
    public String getReleaseDate() {
        return releaseDate;
    }
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
    public String getCast() {
        return cast;
    }
    public void setCast(String cast) {
        this.cast = cast;
    }
    public String getSubtitles() {
        return subtitles;
    }
    public void setSubtitles(String subtitles) {
        this.subtitles = subtitles;
    }
    public String getProducers() {
        return producers;
    }
    public void setProducers(String producers) {
        this.producers = producers;
    }
    public String getWriters() {
        return writers;
    }
    public void setWriters(String writers) {
        this.writers = writers;
    }
    
    
}
