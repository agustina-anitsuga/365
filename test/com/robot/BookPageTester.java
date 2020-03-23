package com.robot;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.robot.page.BookPage;
import com.robot.types.BookRobot;
import com.robot.types.BookScraperRobot;
import com.robot.utils.Browser;
import com.robot.utils.SeleniumUtils;

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
        // https://www.amazon.com/-/es/Delia-Owens/dp/0735219095/ref=zg_bs_books_2/142-1056486-4219225?_encoding=UTF8&psc=1&refRID=SB3SN95NFBCZKEWGMAQM
        // https://www.amazon.com/-/es/Ann-Whitford-Paul/dp/0374300216/ref=zg_bs_books_4/131-1649634-2945747?_encoding=UTF8&psc=1&refRID=TGGEY8D6H5A7M01W4F13
        // https://www.amazon.com/-/es/Kobe-Bryant/dp/0374201234/ref=zg_bs_books_9/141-9397858-0351050?_encoding=UTF8&psc=1&refRID=908BXBMK024V1XD73SWT
        // https://www.amazon.com/-/es/Libro-Hungry-Caterpillar-Junta-libro/dp/0399226907/ref=zg_bs_books_10/134-0771145-0033625?_encoding=UTF8&psc=1&refRID=XB62V3EJESE6S4WXECF3
        // https://www.amazon.com/-/es/Amy-Ramos/dp/1623158087/ref=zg_bs_books_29/147-7714733-0147631?_encoding=UTF8&psc=1&refRID=8DTT1PAFNF3Z5E9KJEEJ
        // https://www.amazon.com/-/es/College-Board/dp/1457312190/ref=zg_bs_books_49/147-7714733-0147631?_encoding=UTF8&psc=1&refRID=8DTT1PAFNF3Z5E9KJEEJ
        // https://www.amazon.com/-/es/Shannon-Roberts/dp/1941325823/ref=zg_bs_books_42/142-3245467-8195831?_encoding=UTF8&psc=1&refRID=TYFSBX7RTH4HQWPFPAFE
        // https://www.amazon.com/-/es/Simon-Rush/dp/1699451958/ref=zg_bs_books_38/142-3245467-8195831?_encoding=UTF8&psc=1&refRID=TYFSBX7RTH4HQWPFPAFE
        // https://www.amazon.com/-/es/Amy-Ramos/dp/1623158087/ref=zg_bs_books_29/142-3245467-8195831?_encoding=UTF8&psc=1&refRID=TYFSBX7RTH4HQWPFPAFE
        // https://www.amazon.com/-/es/Bill-Martin-Jr/dp/0805047905/ref=zg_bs_books_27/147-5971479-1215466?_encoding=UTF8&psc=1&refRID=VXNVB79VE3N9WZMK164J
        // https://www.amazon.com/-/es/Robert-Philips-Sean-Kirkman/dp/8498855152/
        // https://www.amazon.com/-/es/G-Riddle/dp/1940026032/
        // https://www.amazon.com/-/es/Robert-Philips-Sean-Kirkman/dp/8498855152/
        // https://www.amazon.com/-/es/Sidney-Sheldon/dp/1478948434/";
        // https://www.amazon.com/-/es/Master-Game-Sidney-Sheldon/dp/0688013651/ref=tmm_hrd_swatch_0?_encoding=UTF8&qid=1584057206&sr=1-1";
        //String url="https://www.amazon.com/-/es/Sidney-Sheldons-Tides-Memory-Sheldon/dp/006222302X/";
        //String url = "https://www.amazon.com/-/es/Chronicles-Narnia-Box-Set/dp/0060244887/ref=tmm_hrd_swatch_0?_encoding=UTF8&qid=1584070775&sr=1-4";
        //String url = "https://www.amazon.com/-/es/Swing-Life-Times-Benny-Goodman/dp/0393311686/ref=tmm_pap_swatch_0?_encoding=UTF8&qid=1584234184&sr=1-11";
        //String url = "https://www.amazon.com/-/es/Michael-Crichton/dp/0517084791/";
        //String url = "https://www.amazon.com/-/es/Tamara-Stevens/dp/0313375178/";
        //String url = "https://www.amazon.com/-/es/Stephen-King/dp/1451627297/ref=sr_1_19?qid=1582864461&refinements=p_n_feature_browse-bin%3A2656022011&rnid=618072011&s=books&sr=1-19";
        //url = "https://www.amazon.com/-/es/11-22-63-Stephen-King/dp/1451627289/ref=tmm_hrd_swatch_0?_encoding=UTF8&qid=1582864461&sr=1-19";
        //url = "https://www.amazon.com/-/es/11-22-63-Stephen-King/dp/1501120603/ref=tmm_mmp_swatch_0?_encoding=UTF8&qid=1582864461&sr=1-19";
        
        String url = "https://www.amazon.com/Inteligencia-Emocional-trav%C3%A9s-del-Tarot/dp/8409002337/ref=sr_1_1?dchild=1&keywords=Inteligencia+Emocional+A+Traves+Del+Tarot%3A+Las+7&qid=1584468184&s=books&sr=1-1";
        
        try {
            
            driver = SeleniumUtils.buildDriver(Browser.CHROME);
            
            BookRobot robot = new BookScraperRobot();
            robot.login(driver);        

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
