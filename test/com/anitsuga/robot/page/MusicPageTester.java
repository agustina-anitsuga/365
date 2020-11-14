package com.anitsuga.robot.page;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anitsuga.fwk.utils.Browser;
import com.anitsuga.fwk.utils.SeleniumUtils;
import com.anitsuga.robot.model.Music;
import com.anitsuga.robot.types.MusicRobot;

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
        
        //String url = "https://www.amazon.com/-/es/Bon-Jovi/dp/B01GTQZNNM/ref=sr_1_64?dchild=1&fst=as%3Aoff&qid=1602290771&refinements=p_n_binding_browse-bin%3A387647011%2Cp_n_condition-type%3A1294428011%2Cp_85%3A2470955011&rnid=2470954011&rps=1&s=music&sr=1-64";
        //String url = "https://www.amazon.com/-/es/Journey/dp/B000G7PNKO/ref=tmm_abk_swatch_0?_encoding=UTF8&qid=1602293016&sr=1-27";
        String url = "https://www.amazon.com/-/es/Lana-Del-Rey/dp/B009LA1QW6/ref=tmm_acd_swatch_0?_encoding=UTF8&qid=1601095349&sr=1-43";
        
        try {
            
            driver = SeleniumUtils.buildDriver(Browser.CHROME);
            
            MusicRobot robot = new MusicRobot();
            //robot.login(driver);        

            MusicPage musicPage = new MusicPage(driver).go(url);
            if( musicPage != null )
            {
                musicPage.waitForPopups();
                
                Music music = (Music) robot.getProductData(musicPage,driver);
                System.out.println("price (USD):"+musicPage.getPrice()); 
                System.out.println("price (ARS):"+robot.getPublicationPrice(music));
                System.out.println("is amazon price not set?"+robot.amazonPriceIsNotSet(music));
                System.out.println("seller list url:"+musicPage.getSellerListUrl()); 
                System.out.println("availability:"+musicPage.getAvailability());
                System.out.println("genre:"+musicPage.getGenre());
                System.out.println("origin:"+musicPage.getOrigin());
                System.out.println("artist:"+musicPage.getArtist());
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
