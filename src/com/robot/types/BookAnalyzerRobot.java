package com.robot.types;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.robot.RobotProperties;
import com.robot.model.Publication;
import com.robot.utils.Browser;
import com.robot.utils.SeleniumUtils;

/**
 * BookScraperRobot
 * @author agustina.dagnino
 *
 */
public class BookAnalyzerRobot extends BookRobot {

    /**
     * BOOK_LIST_FILE
     */
    private static final String BOOK_LIST_FILE = "book.list.file";
    
    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BookAnalyzerRobot.class.getName());

    @Override
    public List<Publication> scrape() {
        
        List<Publication> ret = new ArrayList<Publication>();
        List<String> bookUrls = readBookURLs();
        
        WebDriver driver = SeleniumUtils.buildDriver(Browser.CHROME);
        login(driver);
        
        int total = bookUrls.size();
        int count = 0;
        for (String bookUrl : bookUrls) {
            LOGGER.info("Reading book ["+(++count)+"/"+total+"] from url "+bookUrl);
            Publication publication = super.getPublication(bookUrl, driver, null, true);
            ret.add(publication);
        }
        
        return ret;
    }

    /**
     * readBookURLs
     * @return
     */
    private List<String> readBookURLs() {
        List<String> ret = new ArrayList<String>();
        RobotProperties config = RobotProperties.getInstance();
        String bookListFile = config.getProperty(BOOK_LIST_FILE);
        BufferedReader br = null;
        try {
            File file = new File(bookListFile);
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

    /**
     * validConfig
     * @see com.robot.types.BookRobot#validConfig()
     */
    @Override
    public boolean validConfig() {
        
        boolean ret = super.validConfig();
        
        RobotProperties config = RobotProperties.getInstance();
        ret &= validatePropertyIsNotEmpty(config,BOOK_LIST_FILE);
        if (ret) {
            String bookListFile = config.getProperty(BOOK_LIST_FILE);
            File dir = new File(bookListFile);
            if( !dir.exists() ){
                LOGGER.error("book.list.file does not exist. Please create it.");
                ret = false;
            }
        }
             
        return ret;
    }
   
    
}
