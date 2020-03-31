package com.anitsuga.robot.page;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anitsuga.fwk.page.Page;
import com.anitsuga.robot.model.SellerQuote;

/**
 * SellerListPage
 * @author agustina
 *
 */
public class SellerListPage extends Page {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SellerListPage.class.getName());
    
    
    @FindBy(xpath = "//div[@class=\"a-row a-spacing-mini olpOffer\"]" )
    private List<WebElement> sellers ;
    
    @FindBy(xpath = "//*[@id=\"raw-platform-refinement-div\"]/fieldset/ul/span/div[data-a-input-name=\"olpCheckbox_new\"]" )
    private WebElement newFilter ;
    
    @FindBy(xpath = "//ul[@class=\"a-pagination\"]/li[@class=\"a-last\"]/a" )
    private WebElement next ;   
    
    
    /**
     * SellerListPage
     * @param driver
     */
    public SellerListPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * go
     * @param url
     * @return
     */
    public SellerListPage go(String url) {
        // go to url
        driver.get(url);

        // return page
        return new SellerListPage(this.driver);
    }

    /**
     * filterNew
     */
    public void filterNew() {
        newFilter.click();
    }

    /**
     * getSellerQuotes
     * @return
     */
    public List<SellerQuote> getSellerQuotes() {
        List<SellerQuote> sellerQuotes = new ArrayList<SellerQuote>();
        for (WebElement webElement : sellers) {
            SellerQuote quote = new SellerQuote();
            WebElement price = webElement.findElement(By.xpath("div[@class=\"a-column a-span2 olpPriceColumn\"]"));
            quote.setPrice(price.getText());
            WebElement seller = webElement.findElement(By.xpath("div[@class=\"a-column a-span2 olpSellerColumn\"]/h3"));
            String sellerName = seller.getText(); 
            if(StringUtils.isEmpty(sellerName)){
                try {
                    WebElement sellerWebElement = webElement.findElement(By.xpath("div[@class=\"a-column a-span2 olpSellerColumn\"]/h3/img"));
                    sellerName = sellerWebElement.getAttribute("alt");
                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                }
            }
            quote.setSeller(sellerName);
            sellerQuotes.add(quote);
        }
        return sellerQuotes;
    }

    /**
     * nextPageUrl
     * @return
     */
    public String nextPageUrl() {
        String url = null;
        try {
            url = next.getAttribute("href");
        } catch(Exception e) {
            LOGGER.error(e.getMessage());
        }
        return url;
    }    

}
