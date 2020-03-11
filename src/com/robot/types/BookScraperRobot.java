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
import com.robot.page.BookCategoryPage;
import com.robot.utils.Browser;
import com.robot.utils.SeleniumUtils;

/**
 * BookScraperRobot
 * @author agustina.dagnino
 *
 */
public class BookScraperRobot extends BookRobot {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BookScraperRobot.class.getName());
    
    /**
     * maximum pages to read at a time
     */
    private static final int MAX_PAGES = 40;

    /**
     * property that points to the file containing the existing publications
     */
    private static final String PUBLISHED_BOOKS_FILE = "published.books.file";
    
    /**
     * existingPublications
     */
    private List<String> existingPublications = new ArrayList<String>();
    
    
    /**
     * scrape
     */
    public List<Publication> scrape() {

        List<Publication> ret = null;

        // Get required properties
        RobotProperties config = RobotProperties.getInstance();
        String url = config.getProperty("book.url");
        
        // Load already published urls if they exist
        loadExistingPublications();
        
        // Scrape data
        ret = scrape(url);
        
        return ret;
    }

    /**
     * loadExistingPublications
     */
    private void loadExistingPublications() {
        RobotProperties config = RobotProperties.getInstance();
        String bookListFile = config.getProperty(PUBLISHED_BOOKS_FILE);
        BufferedReader br = null;
        try {
            File file = new File(bookListFile);
            br = new BufferedReader(new FileReader(file)); 
            String st; 
            while ((st = br.readLine()) != null){ 
                existingPublications.add(st);
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
    }

    /**
     * scrape
     * @param url
     * @return
     */
    private List<Publication> scrape(String url) {
        
        WebDriver driver = null ;
        List<Publication> ret = new ArrayList<Publication>();
        
        try {
            
            driver = SeleniumUtils.buildDriver(Browser.CHROME);
            BookCategoryPage categoryPage = new BookCategoryPage(driver);
            List<String> links = new ArrayList<String>();
            
            int pageCount = 0;
            do {
                LOGGER.info("Reading links from "+url);      
                categoryPage = categoryPage.go(url);
                categoryPage.waitForPopups();
                links.addAll(categoryPage.getLinks());
                pageCount++;
                if(categoryPage.hasNextPage()){
                    url = categoryPage.getNextUrl();
                }
            } while ( categoryPage.hasNextPage() && (pageCount<MAX_PAGES) );
            
            LOGGER.info("Read links "+links.size());
            
            // for each link, extract book
            int total = links.size();
            int count=0;
            for (String link : links) {
                LOGGER.info("Reading book ["+(++count)+"/"+total+"] from url "+link);
                List<Publication> publications = getPublications(link, driver);
                ret.addAll(publications);
            }
            
        } catch (Exception e) {
            
            // take screenshot of error
            SeleniumUtils.captureScreenshot(driver);
            
            // log exception
            LOGGER.error("Error reading URL "+url, e);
            
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
        
        return ret;
    }

    /**
     * shouldIgnoreUrl
     * @param url
     * @return
     */
    protected boolean shouldIgnoreUrl(String url) {
        return super.shouldIgnoreUrl(url) || existingPublications.contains(this.getShortLinkFor(url));
    }
    
    /**
     * filterExistingPublications
     * @param links
     * @return
     */
    /*
    private List<String> filterExistingPublications(List<String> links) {
        List<String> ret = new ArrayList<String>();
        for (String link : links) {
            String shortenedLink = getShortLinkFor(link);
            if(!existingPublications.contains(shortenedLink)){
                ret.add(link);
            }
        }
        return ret;
    }
    */

    /**
     * getShortLinkFor
     * @param link
     * @return
     */
    private String getShortLinkFor(String link) {
        int from = link.indexOf("/dp/");
        int to = link.indexOf("/",from+5);
        return link.substring(0, to+1);
    }

    /**
     * validConfig
     * @return
     */
    public boolean validConfig() {
        boolean ret = super.validConfig();
        
        RobotProperties config = RobotProperties.getInstance();        
        ret &= validatePropertyIsNotEmpty(config,"book.url");

        return ret;
    }



}
