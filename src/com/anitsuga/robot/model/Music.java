package com.anitsuga.robot.model;

/**
 * Music
 * @author agustina
 *
 * https://www.upcitemdb.com/upc/696568418397
 * https://www.synccentric.com/features/upc-asin/
 */
public class Music extends Product {
 
    private String artist;
    private String album;
    private String format ; // Fisico / Digital
    private String albumFormat; // CD, Casete, Vinilo
    private boolean hasAdditionalTracks;
    private String releaseYear; // just year is required
    private String numberOfDisks; // number of albums
    private String numberOfSongs ;
    private String genre;
    private String origin;
    private String label;
    
    
    public String getReleaseYear() {
        return releaseYear;
    }
    public void setReleaseYear(String releaseDate) {
        this.releaseYear = releaseDate;
    }
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public String getNumberOfDisks() {
        return numberOfDisks;
    }
    public void setNumberOfDisks(String numberOfDisks) {
        this.numberOfDisks = numberOfDisks;
    }
    public String getArtist() {
        return artist;
    }
    public void setArtist(String artist) {
        this.artist = artist;
    }
    public String getAlbum() {
        return album;
    }
    public void setAlbum(String album) {
        this.album = album;
    }
    public String getNumberOfSongs() {
        return numberOfSongs;
    }
    public void setNumberOfSongs(String numberOfSongs) {
        this.numberOfSongs = numberOfSongs;
    }
    public boolean hasAdditionalTracks() {
        return hasAdditionalTracks;
    }
    public void setHasAdditionalTracks(boolean hasAdditionalTracks) {
        this.hasAdditionalTracks = hasAdditionalTracks;
    }
    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public String getOrigin() {
        return origin;
    }
    public void setOrigin(String origin) {
        this.origin = origin;
    }
    public String getFormat() {
        return format;
    }
    public void setFormat(String format) {
        this.format = format;
    }
    public String getAlbumFormat() {
        return albumFormat;
    }
    public void setAlbumFormat(String albumFormat) {
        this.albumFormat = albumFormat;
    }
    
}
