package com.anitsuga.robot.writer;

import com.anitsuga.robot.model.Music;
import com.anitsuga.robot.model.Product;

/**
 * MusicPublicationExcelWriter
 * @author agustina.dagnino
 *
 */
public class MusicPublicationExcelWriter extends PublicationExcelWriter  {

    /**
     * fields
     */
    private String[] fields = new String[] {
                   "Nombre del Artista",
                   "Nombre del Album",
                   "Formato",
                   "Formato del Album",
                   "Con pistas adicionales",
                   "Ano de lanzamiento",
                   "Cantidad de albumes",
                   "Cantidad de canciones",       
                   "Genero",
                   "Origen"
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
        Music music = (Music) product;
        ret[0] = music.getArtist();
        ret[1] = music.getAlbum();
        ret[2] = music.getFormat();
        ret[3] = music.getAlbumFormat();
        ret[4] = String.valueOf(music.hasAdditionalTracks());
        ret[5] = music.getReleaseYear();
        ret[6] = String.valueOf(music.getNumberOfDisks());
        ret[7] = String.valueOf(music.getNumberOfSongs());
        ret[8] = music.getGenre();
        ret[9] = music.getOrigin();
        return ret;
    }
    
}
