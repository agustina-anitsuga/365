package com.anitsuga.imager.page;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
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
 * AbeBookPage
 * @author agustina
 *
 */
public class AbeBookPage extends Page {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbeBookPage.class.getName());
    
    
    @FindBy(xpath = "//*[@id=\"isbn\"]/a[2]/span")
    private WebElement isbn;
    
    @FindBy(xpath = "//*[@id=\"viewLarger\"]/a")
    private WebElement imagePopup;
    
    @FindBy(xpath = "/html/body/img" )
    private WebElement image;
    
    @FindBy(xpath = "/html/body/div[3]/div[2]/div[1]/div[2]/div/img[2]" )
    private WebElement image2;
            
    /**
     * AbeBookPage
     * @param driver
     */
    public AbeBookPage(WebDriver driver) {
        super(driver);
    }

    /**
     * go
     * @param url
     * @return
     */
    public AbeBookPage go(String url) {
        // go to url
        driver.get(url);

        // return page
        return new AbeBookPage(this.driver);
    }

    /**
     * getIsbn
     * @return
     */
    public String getIsbn() {
        return isbn.getText();
    }

    /**
     * getImages
     * @return
     */
    public List<String> getImages() {
        
        List<String> ret = new ArrayList<String>();
        String imageStr = null;
        try {
            imagePopup.click();
            
            WebDriverWait wait = SeleniumUtils.getWait(driver,5);
            wait.until(ExpectedConditions.presenceOfElementLocated( 
                    By.xpath("/html/body/img") ));
   
            imageStr = image.getAttribute("src");
            ret.add(imageStr);
        } catch (Exception e) {
            try {
                WebDriverWait wait = SeleniumUtils.getWait(driver,10);
                wait.until(ExpectedConditions.presenceOfElementLocated( 
                        By.xpath("/html/body/div[3]/div[2]/div[1]/div[2]/div/img[2]") ));
               
                imageStr = image2.getAttribute("src");
                ret.add(imageStr);
            } catch (Exception e2) {
                LOGGER.debug(e2.getMessage());
            }
        }
        return ret;
    }
}
