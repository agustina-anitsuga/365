package com.anitsuga.invoicer.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import com.anitsuga.fwk.page.Page;

/**
 * ProductTypePage
 * @author agustina
 *
 */
public class ProductTypePage extends Page {

    @FindBy( xpath = "//*[@id=\"idconcepto\"]")
    private WebElement productType;
    
    @FindBy( xpath = "//*[@id=\"contenido\"]/form/input[2]")
    private WebElement nextButton;

    /**
     * ProductTypePage
     * @param driver
     */
    public ProductTypePage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * go
     * @param url
     * @return
     */
    public ProductTypePage go(String url) {
        // go to url
        driver.get(url);

        // return page
        return new ProductTypePage(this.driver);
    }

    /**
     * selectDefaultProductType
     */
    public void selectDefaultProductType() {
        Select productTypeSelect = new Select(productType);
        productTypeSelect.selectByVisibleText(" Productos");
    }

    /**
     * clickNext
     */
    public void clickNext() {
        nextButton.click();
    }
    
}
