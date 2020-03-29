package com.anitsuga.meli.page;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anitsuga.page.Page;
import com.anitsuga.utils.SeleniumUtils;

/**
 * PublicationPage
 * @author agustina
 *
 */
public class PublicationPage extends Page {

    
    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PublicationPage.class.getName());

    
    @FindBy( xpath = "//*[@id=\"price_and_currency\"]/div[2]/label/div[1]/input" )
    private WebElement price;
    
    @FindBy( xpath = "//*[@id=\"quick_edit_standard_task\"]/div[2]/div[4]/button[1]" )
    private WebElement saveButton;
    
    
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
     * setPrice
     * @param price
     */
    public void setPrice(Number priceArg) {
        price.sendKeys(Keys.CONTROL,"a");
        price.sendKeys(Keys.DELETE);
        price.sendKeys(priceArg.toString());
    }

    /**
     * commit
     */
    public void commit() {
        saveButton.click();
    }    

    /**
     * waitForLoad
     */
    public void waitForLoad(){
        try {
            String buttonXPath = "//*[@id=\"info_header_container\"]/div[1]/h2/a";
            WebDriverWait wait = SeleniumUtils.getWait(driver,30);
            wait.until(ExpectedConditions.presenceOfElementLocated( 
                    By.xpath(buttonXPath) ));
        } catch ( Exception e ){
            LOGGER.debug(e.getMessage());
        }
    }
}
