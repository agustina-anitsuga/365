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

import com.anitsuga.fwk.utils.SeleniumUtils;
import com.anitsuga.robot.RobotURLProvider;
import com.anitsuga.robot.page.CategoryPage;

/**
 * WebScraperURLProvider
 * @author agustina
 *
 */
public class WebScraperURLProvider implements RobotURLProvider {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebScraperURLProvider.class.getName());

    /**
     * MAX_PAGES
     */
    private static final int MAX_PAGES = 40;
    
    /**
     * URL where the robot will start scraping
     */
    private String startingPointUrl;
    
    /**
     * file name with already published products
     */
    private String alreadyPublishedProductsFile;
    
    /**
     * existingPublications
     */
    private List<String> existingPublications = new ArrayList<String>();
    
    /**
     * driver
     */
    private WebDriver driver;
    
    
    public WebScraperURLProvider( String startingPointUrl, String alreadyPublishedProductsFile ){
        this.startingPointUrl = startingPointUrl;
        this.alreadyPublishedProductsFile = alreadyPublishedProductsFile;
    }
    
    
    /**
     * setWebDriver
     */
    public void setWebDriver( WebDriver driver ){
        this.driver = driver;
    }
    
    
    @Override
    public List<String> getURLs() {
        
        // Load already published urls if they exist
        loadExistingPublications();
        
        // Initialize result
        List<String> links = new ArrayList<String>();
        String url = startingPointUrl;
        
        try {
            CategoryPage categoryPage = new CategoryPage(driver);
            
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
            
        } catch (Exception e) {
            
            // take screenshot of error
            SeleniumUtils.captureScreenshot(driver);
            // log exception
            LOGGER.error("Error reading URL "+url, e);
        } 
        
        return links;
    }

    /**
     * loadExistingPublications
     */
    private void loadExistingPublications() {
        BufferedReader br = null;
        try {
            File file = new File(alreadyPublishedProductsFile);
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


    @Override
    public boolean shouldIgnoreUrl(String url) {
        return existingPublications.contains(this.getAmazonId(url));
    }
    
    /**
     * getAmazonId
     * @param link
     * @return
     */
    protected String getAmazonId(String link) {
        int from = link.indexOf("/dp/");
        int to = link.indexOf("/",from+5);
        return link.substring(from+4, to);
    }
}
