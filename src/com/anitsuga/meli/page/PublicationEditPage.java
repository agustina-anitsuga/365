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

import com.anitsuga.fwk.page.Page;
import com.anitsuga.fwk.utils.SeleniumUtils;

/**
 * PublicationPage
 * @author agustina
 *
 */
public class PublicationEditPage extends Page {

    
    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PublicationEditPage.class.getName());

    @FindBy( xpath = "//*[@id=\"info_header_container\"]/div[1]/h2/a" )    
    private WebElement title;
    
    @FindBy( xpath = "//*[@id=\"price_and_currency\"]/div[2]/label/div[1]/input" )
    private WebElement price;
    
    @FindBy( xpath = "//*[@id=\"quick_edit_standard_task\"]/div[2]/div[4]/button[1]" )
    private WebElement savePriceButton;

    @FindBy( xpath = "//*[@id=\"quick_edit_standard_task\"]/div[2]/div[5]/button[1]" )
    private WebElement savePriceButton2;
    
    @FindBy( xpath = "//*[@id=\"detail_layout\"]/div/p/span[@class=\"sell-ui-snackbar__message\"]" )
    private WebElement message;
    
    @FindBy( xpath = "//*[@id=\"manufacturing_time_task\"]" )
    private WebElement availabilitySection;
    
    @FindBy( xpath = "//*[@id=\"manufacturing_time_task\"]/div[2]/div[1]/div[2]/label/div[1]/input" )
    private WebElement availability;
    
    @FindBy( xpath = "//*[@id=\"manufacturing_time_task\"]/div[2]/div[2]/button[1]" )
    private WebElement saveAvailabilityButton;
    
    @FindBy( xpath = "//*[@id=\"detail_layout\"]/div[1]/div[1]/div/div[3]/div/a" )
    private WebElement reactivateButton;
    
    
    /**
     * PublicationPage
     * @param driver
     */
    public PublicationEditPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * go
     * @param url
     * @return
     */
    public PublicationEditPage go(String url) {
        // go to url
        driver.get(url);

        // return page
        return new PublicationEditPage(this.driver);
    }

    /**
     * setPrice
     * @param price
     */
    public void setPrice(String priceArg) {
        String existingPrice = getPriceValue();
        int length = existingPrice.length();
        for (int i = 0; i < length; i++) {
            price.sendKeys(Keys.BACK_SPACE);
        }
        price.sendKeys(priceArg);
    }

    /**
     * getPriceValue
     */
    public String getPriceValue(){
        return price.getAttribute("value");
    }
    
    /**
     * getPrice
     */
    public String getPrice(){
        return price.getText();
    }
    
    /**
     * commit
     */
    public void commit() {
        try {
            savePriceButton.click();
        } catch (Exception e) {
            LOGGER.debug(e.getMessage());
            savePriceButton2.click();
        }
    }    

    /**
     * waitForLoad
     */
    public void waitForLoad(){
        try {
            String titleXPath = "//*[@id=\"info_header_container\"]/div[1]/h2/a";
            WebDriverWait wait = SeleniumUtils.getWait(driver,60);
            wait.until(ExpectedConditions.presenceOfElementLocated( 
                    By.xpath(titleXPath) ));
        } catch ( Exception e ){
            LOGGER.debug(e.getMessage());
        }
    }
    
    /**
     * waitForSave
     */
    public String waitForSave(){
        String ret = "";
        try {
            WebDriverWait wait = SeleniumUtils.getWait(driver,60);
            // wait.until(ExpectedConditions.not(ExpectedConditions.elementToBeClickable(saveButton)) );
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"detail_layout\"]/div/p/span[@class=\"sell-ui-snackbar__message\"]")));
            ret = message.getText();
        } catch ( Exception e ){
            LOGGER.debug(e.getMessage());
        }
        return ret;
    }

    /**
     * getTitle
     * @return
     */
    public String getTitle() {
        return title.getText();
    }

    /**
     * setAvailability
     * @param expectedAvailability
     */
    public void setAvailability(String expectedAvailability) {
        String existingAvailability = getAvailability();
        int length = existingAvailability.length();
        for (int i = 0; i < length; i++) {
            availability.sendKeys(Keys.BACK_SPACE);
        }
        availability.sendKeys(expectedAvailability);
    }

    /**
     * getAvailability
     * @return
     */
    public String getAvailability() {
        return availability.getAttribute("value");
    }

    /**
     * openAvailabilitySection
     */
    public void openAvailabilitySection() {
        availabilitySection.click();
    }

    /**
     * commitAvailability
     */
    public void commitAvailability(){
        saveAvailabilityButton.click();
    }
    
    /**
     * reactivate
     */
    public void reactivate(){
        reactivateButton.click();
    }
    
}
