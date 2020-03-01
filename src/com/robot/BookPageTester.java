package com.robot;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.robot.page.BookPage;
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
        String url = "https://www.amazon.com/-/es/G-Riddle/dp/1940026032/";
        
        try {
            
            driver = SeleniumUtils.buildDriver(Browser.CHROME);
        
            BookPage bookPage = new BookPage(driver).go(url);
        
            if( bookPage != null )
            {
                bookPage.waitForPopups();
                System.out.println("author:"+bookPage.getAuthor());
                System.out.println("availability:"+bookPage.getAvailability());
                System.out.println("price:"+bookPage.getPrice());
                System.out.println("type:"+bookPage.getType());
                System.out.println("lang:"+bookPage.getLanguage());
                System.out.println("type:"+bookPage.getType());
                System.out.println("title:"+bookPage.getTitle());
                System.out.println("title.len:"+bookPage.getTitle().length());
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
