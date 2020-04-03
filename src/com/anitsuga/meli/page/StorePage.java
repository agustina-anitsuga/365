package com.anitsuga.meli.page;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.anitsuga.fwk.page.Page;

/**
 * StorePage
 * @author agustina
 *
 */
public class StorePage extends Page {
    
    @FindBy ( xpath = "/html/body/header/div/form/input" )
    private WebElement searchBox;
    
    @FindBy ( xpath = "//*[@id=\"categorySearch\"]" )
    private WebElement stayOnStoreCheckBox;

    @FindBy ( xpath = "/html/body/header/div/form/button[@class=\"nav-search-btn\"]" )
    private WebElement searchButton;
    
    @FindBy ( xpath = "//*[@id=\"searchResults\"]/li/div/div/div/div/a" )
    private List<WebElement> searchResults;
    
    /**
     * StorePage
     * @param driver
     */
    public StorePage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * go
     * @param url
     * @return
     */
    public StorePage go(String url) {
        // go to url
        driver.get(url);

        // return page
        return new StorePage(this.driver);
    }
    
    /**
     * setSearch
     * @param text
     */
    public void setSearch(String text){
         searchBox.sendKeys(text);
         if( !stayOnStoreCheckBox.isSelected() ){
             stayOnStoreCheckBox.click();
         }
    }
    
    /**
     * doSearch
     * @return
     */
    public StorePage doSearch(){
        searchButton.click();
        return new StorePage(driver);
    }

    /**
     * getSearchResultUrls
     * @return
     */
    public List<String> getSearchResultUrls() {
        List<String> ret = new ArrayList<String>();
        for (WebElement webElement : searchResults) {
            String url = webElement.getAttribute("href");
            ret.add(url);
        }
        return ret;
    }


}
