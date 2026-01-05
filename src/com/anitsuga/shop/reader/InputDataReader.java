package com.anitsuga.shop.reader;

import com.anitsuga.shop.model.Listing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * InputDataReader
 * @author agustina
 *
 */
public class InputDataReader {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(InputDataReader.class.getName());
    
    /**
     * read
     * @param filename
     * @return
     */
    public List<Listing> read(String filename) {
        List<Listing> ret = new ArrayList<Listing>();
        BufferedReader br = null;
        try {
            File file = new File(filename);
            br = new BufferedReader(new FileReader(file)); 
            String st; 
            while ((st = br.readLine()) != null){
                Listing product = new Listing();
                product.setId(st);
                ret.add(product);
            } 
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }
    
        return ret;
    }

}
