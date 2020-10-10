package com.anitsuga.robot.page;

import java.util.List;

import org.openqa.selenium.WebDriver;

import com.anitsuga.fwk.page.Page;

/**
 * ProductPage
 * @author agustina
 *
 */
public abstract class ProductPage extends Page {

    /**
     * ProductPage
     * @param driver
     */
    public ProductPage(WebDriver driver) {
        super(driver);
    }
    
    public abstract void waitForPopups();
    
    public abstract List<String> getEditionsUrls();

    public abstract void openPhotoViewer();
    
    public abstract List<String> getImages();
    
    public abstract String getSellerListUrl();
}
