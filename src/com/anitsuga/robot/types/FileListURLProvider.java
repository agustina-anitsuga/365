package com.anitsuga.robot.types;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anitsuga.robot.RobotURLProvider;

/**
 * FileListURLProvider
 * @author agustina
 *
 */
public class FileListURLProvider implements RobotURLProvider {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FileListURLProvider.class.getName());

    /**
     * fileName
     */
    private String fileName;
    
    /**
     * FileListURLProvider
     * @param fileName
     */
    public FileListURLProvider( String fileName ){
        this.fileName = fileName;
    }
    
    
    @Override
    public List<String> getURLs() {
        
        List<String> ret = new ArrayList<String>();
        BufferedReader br = null;
        try {
            File file = new File(fileName);
            br = new BufferedReader(new FileReader(file)); 
            String st; 
            while ((st = br.readLine()) != null){ 
                ret.add(st);
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


    @Override
    public void setWebDriver(WebDriver driver) {
        // do nothing
    }


    @Override
    public boolean shouldIgnoreUrl(String url) {
        return false;
    }
    
}
