package com.anitsuga.robot.writer;

import com.anitsuga.robot.model.Movie;
import com.anitsuga.robot.model.Product;

/**
 * MusicPublicationExcelWriter
 * @author agustina.dagnino
 *
 */
public class MoviePublicationExcelWriter extends PublicationExcelWriter  {

    /**
     * fields
     */
    private String[] fields = new String[] {
            "Format",
            "Title",
            "Director",
            "Video Resolution",
            "Number of Disks",
            "Audio",
            "Genre",
            "Studio",
            "Physical/Digital"
    };

    /**
     * getFields
     * @return
     */
    @Override
    protected String[] getProductSpecificFields() {
        return this.fields;
    }
    
    /**
     * getProductSpecificValues
     */
    @Override
    protected String[] getProductSpecificValues( Product product ) {
        String[] ret = new String[fields.length];
        Movie movie = (Movie) product;
        ret[0] = movie.getFormat();
        ret[1] = movie.getTitle();
        ret[2] = movie.getDirector();
        ret[3] = movie.getVideoResolution();
        ret[4] = movie.getNumberOfDisks();
        ret[5] = movie.getAudio();
        ret[6] = movie.getGenre();
        ret[7] = movie.getStudio();
        ret[8] = "Físico";
        return ret;
    }
    
}
