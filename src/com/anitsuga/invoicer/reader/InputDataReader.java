package com.anitsuga.invoicer.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anitsuga.invoicer.model.Sale;


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
    public List<Sale> read(String filename) {
        List<Sale> ret = new ArrayList<Sale>();
        BufferedReader br = null;
        try {
            File file = new File(filename);
            br = new BufferedReader(new FileReader(file)); 
            String st; 
            while ((st = br.readLine()) != null){ 
                Sale sale = new Sale();
                sale.setId(st);
                ret.add(sale);
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
