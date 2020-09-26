package com.anitsuga.robot.page;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anitsuga.fwk.utils.Browser;
import com.anitsuga.fwk.utils.SeleniumUtils;
import com.anitsuga.robot.model.Music;
import com.anitsuga.robot.types.MusicRobot;
import com.anitsuga.robot.types.MusicScraperRobot;

/**
 * MusicPageTester
 * @author agustina.dagnino
 *
 */
public class MusicPageTester {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MusicPageTester.class.getName());
    
    /**
     * main
     * @param args
     */
    public static void main(String[] args) {
        
        WebDriver driver = null ;
        
        String url = "https://www.amazon.com/-/es/Pink-Floyd/dp/B01IOED4BK/ref=tmm_vnl_swatch_0?_encoding=UTF8&qid=1601073780&sr=1-1";
        
        try {
            
            driver = SeleniumUtils.buildDriver(Browser.CHROME);
            
            MusicRobot robot = new MusicScraperRobot();
            //robot.login(driver);        

            MusicPage musicPage = new MusicPage(driver).go(url);
            if( musicPage != null )
            {
                Music music = robot.getMusicData(musicPage,driver);
                //bookPage.waitForPopups();
                System.out.println("price (USD):"+musicPage.getPrice()); 
                System.out.println("price (ARS):"+robot.getPublicationPrice(music));
                System.out.println("is amazon price not set?"+robot.amazonPriceIsNotSet(music));
                System.out.println("seller list url:"+musicPage.getSellerListUrl()); 
                System.out.println("availability:"+musicPage.getAvailability());
                System.out.println("genre:"+musicPage.getGenre());
                System.out.println("origin:"+musicPage.getOrigin());
                System.out.println("seller:"+musicPage.getSeller());
                System.out.println("album title:"+musicPage.getAlbum());
                System.out.println("title.len:"+musicPage.getAlbum().length());
                System.out.println("albumFormat:"+musicPage.getAlbumFormat());
                System.out.println("hasAdditionalTracks:"+musicPage.hasAdditionalTracks());
                System.out.println("releaseYear:"+musicPage.getReleaseYear());
                System.out.println("numberOfDisks:"+musicPage.getNumberOfDisks());
                System.out.println("numberOfSongs:"+musicPage.getNumberOfSongs());                
                List<String> editions = musicPage.getEditionsUrls();
                System.out.println("Editions");
                for (String edition : editions) {
                    System.out.println("    edition="+edition);
                }
                System.out.println("Images");
                musicPage.openPhotoViewer();
                List<String> images = musicPage.getImages();
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
