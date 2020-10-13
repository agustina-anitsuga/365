package com.anitsuga.tools.page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.anitsuga.fwk.page.Page;

/**
 * HiperChinoPage
 * @author agustina
 *
 */
public class HiperChinoPage extends Page {

    @FindBy(xpath = "//table[@class=\"product-detail table\"]/tbody/tr" )
    private List<WebElement> productDetailsTable;
    
    private Map<String,String> productDetails;
    
    
    /**
     * HiperChinoPage
     * @param driver
     */
    public HiperChinoPage(WebDriver driver) {
        super(driver);
    }

    /**
     * go
     * @param url
     * @return
     */
    public HiperChinoPage go(String url) {
        // go to url
        driver.get(url);

        // return page
        return new HiperChinoPage(this.driver);
    }
    
    /**
     * loadProductDetails
     */
    private void loadProductDetails() {
        if( productDetails == null ){
            productDetails = new HashMap<String,String>();
            for (WebElement productDetail : productDetailsTable) {
                String key = productDetail.findElement(By.xpath("./th")).getText();
                String value = productDetail.findElement(By.xpath("./td")).getText();
                productDetails.put(key,value);
            }
        }
    }

    /**
     * getUpc
     * @return
     */
    public String getUpc() {
        this.loadProductDetails();
        return productDetails.get("EAN");
    }

    /**
     * getReleaseYear
     * @return
     */
    public String getReleaseYear() {
        this.loadProductDetails();
        return productDetails.get("Fecha Lanzamiento");
    }

}
