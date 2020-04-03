package com.anitsuga.meli.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.anitsuga.fwk.page.Page;

/**
 * PublicationPage
 * @author agustina
 *
 */
public class PublicationPage extends Page {

    @FindBy( xpath = "//*[@id=\"short-desc\"]/div/header/h1" )
    private WebElement title;

    @FindBy( xpath = "//ul[@class=\"specs-list specs-list-secondary specs-structure-xlarge\"]/li[2]/span" )
    private WebElement isbn;
    
    
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

    /**
     * getIsbn
     * @return
     */
    public String getIsbn() {
        String ret = title.getText();
        return ret;
    }

    /**
     * getTitle
     * @return
     */
    public String getTitle() {
        String ret = title.getText();
        return ret;
    }
    
    
}
