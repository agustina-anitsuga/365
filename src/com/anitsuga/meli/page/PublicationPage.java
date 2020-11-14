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
    private WebElement title1;
    @FindBy( xpath = "//h1[@class=\"ui-pdp-title\"]" )
    private WebElement title2;
    private WebElement[] title = new WebElement[]{title1,title2};
    

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
        String ret = isbn.getText();
        return ret;
    }

    /**
     * getTitle
     * @return
     */
    public String getTitle() {
        String ret = null;
        for (int i = 0; i < title.length; i++) {
            try {
                WebElement ti = title[i];
                ret = ti.getText();
                if( ret!=null ){
                    break;
                }
            } catch (Exception e) {
                // nothing
            }
        }
        return ret;
    }
    
    
}
