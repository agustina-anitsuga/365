package com.anitsuga.imager.page;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anitsuga.fwk.page.Page;

/**
 * AbeSearchResultPage
 * @author agustina
 *
 */
public class AbeSearchResultPage extends Page {


    @FindBy(xpath = "//*[@class=\"cf result\"]/div[2]/div[1]/h2/a")
    private List<WebElement> bookLinks;
    
    @FindBy(xpath = "//*[@id=\"book-1\"]/div[2]/div[1]/h2/a")
    private WebElement firstLink;
    
    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbeSearchResultPage.class.getName());
    
    /**
     * AbeSearchResultPage
     * @param driver
     */
    public AbeSearchResultPage(WebDriver driver) {
        super(driver);
        // TODO Auto-generated constructor stub
    }
    
    /**
     * go
     * @param url
     * @return
     */
    public AbeSearchResultPage go(String url) {
        // go to url
        driver.get(url);

        // return page
        return new AbeSearchResultPage(this.driver);
    }

    /**
     * getBookUrls
     * @return
     */
    public List<String> getBookUrls(){
        List<String> ret = new ArrayList<String>();
        if( bookLinks.size() > 0 ){
            for (WebElement bookLink : bookLinks) {
                String url = bookLink.getAttribute("href");
                ret.add(url);
            }
        } 
        if( ret.isEmpty() ){
            String firstLinkStr = firstLink.getAttribute("href");
            if( !StringUtils.isEmpty(firstLinkStr) ){
                ret.add(firstLinkStr);
            }
        }
        return ret;
    }
    
}
