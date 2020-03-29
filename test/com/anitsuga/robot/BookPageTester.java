package com.anitsuga.robot;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anitsuga.robot.page.BookPage;
import com.anitsuga.robot.types.BookRobot;
import com.anitsuga.robot.types.BookScraperRobot;
import com.anitsuga.utils.Browser;
import com.anitsuga.utils.SeleniumUtils;

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
        
        String url = "https://www.amazon.com/Inteligencia-Emocional-trav%C3%A9s-del-Tarot/dp/8409002337/ref=sr_1_1?dchild=1&keywords=Inteligencia+Emocional+A+Traves+Del+Tarot%3A+Las+7&qid=1584468184&s=books&sr=1-1";
        
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
