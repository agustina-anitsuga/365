package com.robot.page;

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

import com.robot.utils.SeleniumUtils;

/**
 * BookCategoryPage
 * @author agustina.dagnino
 *
 */
public class BookCategoryPage extends Page {


    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BookCategoryPage.class.getName());
    
    
    /* [@class=\"a-link-normal\"] */
    @FindBy(xpath = "//*[@class=\"zg-item-immersion\"]/span/div/span/a")
    private List<WebElement> bookLinks;
    

    @FindBy(xpath = "//*[@class=\"a-fixed-left-grid-col a-col-left\"]/div[@class=\"a-row\"]/div/a" )
    private List<WebElement> bookLinks2;
    
    @FindBy(xpath = "//*[@class=\"sg-col-inner\"]/div/span/a")
    private List<WebElement> bookLinks3;
    
    @FindBy(xpath = "//*[@class=\"a-pagination\"]/li[@class=\"a-last\"]/a")
    private WebElement next;
    
    @FindBy(xpath = "//*[@id=\"pagnNextLink\"]")
    private WebElement next1;
    
    /**
     * BookCategoryPage
     * @param driver
     */
    public BookCategoryPage(WebDriver driver) {
        super(driver);
    }

    /**
     * go
     * @param url
     * @return
     */
    public BookCategoryPage go(String url) {
        // go to url
        driver.get(url);

        // return page
        return new BookCategoryPage(this.driver);
    }

    /**
     * waitForPopups
     */
    public void waitForPopups() {
        String[] buttons = new String[] {
                "//*[@id=\"nav-main\"]/div[1]/div[2]/div/div[3]/span[1]/span/input"};
        
        for (int i = 0; i < buttons.length; i++) {
            try {
                String dismissButtonXPath = buttons[i];
                WebDriverWait wait = SeleniumUtils.getWait(driver);
                wait.until(ExpectedConditions.presenceOfElementLocated( 
                        By.xpath(dismissButtonXPath) ));
                WebElement element = driver.findElement(By.xpath(dismissButtonXPath));
                element.click();
            } catch ( Exception e ){
                LOGGER.debug(e.getMessage());
            }
        }
    }
    
    
    /**
     * getLinks
     * @return
     */
    public List<String> getLinks() {
        List<String> ret = new ArrayList<String>();
        
        if( bookLinks.size() > 0 ){
            for (WebElement bookLink : bookLinks) {
                String url = bookLink.getAttribute("href");
                ret.add(url);
            }
        } else {
            if( bookLinks2.size() > 0 ){
                for (WebElement bookLink : bookLinks2) {
                    String url = bookLink.getAttribute("href");
                    ret.add(url);
                }
            } else {
                if( bookLinks3.size() > 0 ){
                    for (WebElement bookLink : bookLinks3) {
                        String url = bookLink.getAttribute("href");
                        ret.add(url);
                    }
                }
            }
        }
        return ret;
    }

    /**
     * hasNextPage
     * @return
     */
    public boolean hasNextPage() {
        return hasNextPage0() || hasNextPage1();
    }
    
    /**
     * hasNextPage0
     * @return
     */
    private boolean hasNextPage0() {
        boolean ret = false;
        try {
            ret = next.isEnabled();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return ret;
    }

    /**
     * hasNextPage1
     * @return
     */
    private boolean hasNextPage1() {
        boolean ret = false;
        try {
            ret = next1.isEnabled();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return ret;
    }

    /**
     * getNextUrl
     * @return
     */
    public String getNextUrl(){
        String ret = null;
        if ( hasNextPage0() )
            ret = next.getAttribute("href");
        else if( hasNextPage1() )
            ret = next1.getAttribute("href");
        return ret;
    }
    
}
