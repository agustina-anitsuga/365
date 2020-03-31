package com.anitsuga.meli.reader;

import java.util.ArrayList;
import java.util.List;

import com.anitsuga.meli.model.Publication;

/**
 * PriceUpdaterInputReader
 * @author agustina
 *
 */
public class PriceUpdaterInputReader {

    /**
     * read
     * @param filename
     * @return
     */
    public List<Publication> read(String filename) {

        List<Publication> data = new ArrayList<Publication>();
        
        Publication publication = new Publication();
        publication.setId("MLA845929856");
        publication.setPrice(82);
        publication.setTitle("Libro - Prueba - No Comprar");
        data.add(publication);
        
        publication = new Publication();
        publication.setId("MLA845929856");
        publication.setPrice(23);
        publication.setTitle("Libro - Prueba - No Comprar");
        data.add(publication);

        publication = new Publication();
        publication.setId("MLA845929856");
        publication.setPrice(82);
        publication.setTitle("Libro - Prueba - No Comprar");
        data.add(publication);
        
        publication = new Publication();
        publication.setId("MLA845929856");
        publication.setPrice(23);
        publication.setTitle("Libro - Prueba - No Comprar");
        data.add(publication);
        
        return data;
    }

}
