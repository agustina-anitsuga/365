package com.anitsuga.robot.page;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anitsuga.fwk.utils.Browser;
import com.anitsuga.fwk.utils.SeleniumUtils;
import com.anitsuga.robot.page.BookPage;
import com.anitsuga.robot.types.BookRobot;
import com.anitsuga.robot.types.BookScraperRobot;

/**
 * PageTester
 * @author agustina.dagnino
 *
 */
public class BookPageTester {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BookPageTester.class.getName());
    
    /**
     * main
     * @param args
     */
    public static void main(String[] args) {
        
        WebDriver driver = null ;
        
        String url = "https://www.amazon.com/-/es/Francisco-Ugarte-Corcuera/dp/6078237977/ref=sr_1_98?dchild=1&fst=as%3Aoff&qid=1586715639&refinements=p_n_feature_nine_browse-bin%3A3291439011%2Cp_85%3A2470955011%2Cp_n_availability%3A2245265011%2Cp_n_condition-type%3A1294423011%2Cp_n_feature_browse-bin%3A2656022011&rnid=4736&rps=1&s=books&sr=1-98";
        
        try {
            
            driver = SeleniumUtils.buildDriver(Browser.CHROME);
            
            BookRobot robot = new BookScraperRobot();
            //robot.login(driver);        

            BookPage bookPage = new BookPage(driver).go(url);
            if( bookPage != null )
            {
                //bookPage.waitForPopups();
                System.out.println("seller list url:"+bookPage.getSellerListUrl());
                System.out.println("author:"+bookPage.getAuthor());
                System.out.println("availability:"+bookPage.getAvailability());
                System.out.println("price (USD):"+bookPage.getPrice());
                System.out.println("price (ARS):"+robot.getPublicationPrice(robot.getBookData(bookPage,driver)));
                System.out.println("type:"+bookPage.getType());
                System.out.println("lang:"+bookPage.getLanguage());
                System.out.println("type:"+bookPage.getType());
                System.out.println("seller:"+bookPage.getSeller());
                System.out.println("title:"+bookPage.getTitle());
                System.out.println("title.len:"+bookPage.getTitle().length());
                List<String> editions = bookPage.getEditionsUrls();
                System.out.println("Editions");
                for (String edition : editions) {
                    System.out.println("    edition="+edition);
                }
                System.out.println("Images");
                bookPage.openPhotoViewer();
                List<String> images = bookPage.getImages();
                for (String image : images) {
                    System.out.println("    image - "+image);
                }

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
    }
}
