package com.robot.writer;

import java.util.List;

import com.robot.model.Publication;

/**
 * PublicationCSVWriter
 * @author agustina.dagnino
 *
 */
public interface PublicationCSVWriter {


    /**
     * QUOTES
     */
    static final String QUOTES = "\"";

    /**
     * DELIMITER
     */
    static final String DELIMITER = ";";

    
    public void write( String filename, List<Publication> publications );
    
}
