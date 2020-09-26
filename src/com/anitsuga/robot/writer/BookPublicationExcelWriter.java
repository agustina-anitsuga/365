package com.anitsuga.robot.writer;

import com.anitsuga.robot.model.Book;
import com.anitsuga.robot.model.Product;

/**
 * BookPublicationExcelWriter
 * @author agustina.dagnino
 *
 */
public class BookPublicationExcelWriter extends PublicationExcelWriter {
    
    /**
     * fields
     */
    private String[] fields = new String[] {
                   "Tipo de narración",
                   "Título",
                   "Autor",
                   "Idioma",
                   "Editorial",
                   "Formato",
                   "Tipo de tapa"
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
        Book book = (Book) product;
        ret[0] = book.getGenre();
        ret[1] = book.getTitle();
        ret[2] = book.getAuthor();
        ret[3] = book.getLanguage();
        ret[4] = book.getEditorial();
        ret[5] = book.getFormat();
        ret[6] = book.getCover();
        return ret;
    }
    
    
}
