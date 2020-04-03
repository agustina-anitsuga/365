package com.anitsuga.meli.page;

import org.openqa.selenium.WebDriver;

import com.anitsuga.fwk.page.Page;

/**
 * PublicationPage
 * @author agustina
 *
 */
public class PublicationPage extends Page {

    /**
     * PublicationPage
     * @param driver
     */
    public PublicationPage(WebDriver driver) {
        super(driver);
    }

    /**
     * go
     * @param url
     * @return
     */
    public PublicationPage go(String url) {
        // go to url
        driver.get(url);

        // return page
        return new PublicationPage(this.driver);
    }
    
    
}
