package com.anitsuga.robot.page;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anitsuga.fwk.utils.Browser;
import com.anitsuga.fwk.utils.SeleniumUtils;
import com.anitsuga.robot.model.Movie;
import com.anitsuga.robot.types.MovieRobot;

/**
 * MoviePageTester
 * @author agustina.dagnino
 *
 */
public class MoviePageTester {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MoviePageTester.class.getName());
    
    /**
     * main
     * @param args
     */
    public static void main(String[] args) {
        
        WebDriver driver = null ;
        
        //String url = "https://www.amazon.com/-/es/Richard-Attenborough/dp/B00VFZU9X2/ref=tmm_blu_swatch_0?_encoding=UTF8&qid=1606356554&sr=1-1-spons";
        String url = "https://www.amazon.com/-/es/Sam-Neill/dp/B07FQCQT5M/ref=tmm_blu_swatch_0?_encoding=UTF8&qid=1606356554&sr=1-2-spons";
        
        try {
            
            driver = SeleniumUtils.buildDriver(Browser.CHROME);
            
            MovieRobot robot = new MovieRobot();
           
            MoviePage moviePage = new MoviePage(driver).go(url);
            if( moviePage != null )
            {
                moviePage.waitForPopups();
                
                Movie movie = (Movie) robot.getProductData(moviePage,driver);
                System.out.println("price (USD):"+moviePage.getPrice()); 
                System.out.println("price (ARS):"+robot.getPublicationPrice(movie));
                System.out.println("is amazon price not set?"+robot.amazonPriceIsNotSet(movie));
                System.out.println("seller list url:"+moviePage.getSellerListUrl()); 
                System.out.println("availability:"+moviePage.getAvailability());
                System.out.println("genre:"+moviePage.getGenre());
                System.out.println("seller:"+moviePage.getSeller());
                List<String> editions = moviePage.getEditionsUrls();
                System.out.println("Editions");
                for (String edition : editions) {
                    System.out.println("    edition="+edition);
                }
                System.out.println("Images");
                moviePage.openPhotoViewer();
                List<String> images = moviePage.getImages();
                for (String image : images) {
                    System.out.println("    image - "+image);
                }

            }    
            
        } catch (Exception e) {
            
            // take screenshot of error
            SeleniumUtils.captureScreenshot(driver);
            
            // log exception
            LOGGER.error("Error reading a URL "+url, e);
            
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
