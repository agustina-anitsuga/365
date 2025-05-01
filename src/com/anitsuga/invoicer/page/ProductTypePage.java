package com.anitsuga.invoicer.page;

import com.anitsuga.fwk.utils.AppProperties;
import com.anitsuga.fwk.utils.StringUtils;
import org.openqa.selenium.Keys;
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

    @FindBy( xpath = "//*[@id=\"fc\"]")
    private WebElement date;

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

    public void setDate() {
        String forcedInvoiceDate = AppProperties.getInstance().getProperty("invoice.date");
        if(!StringUtils.isEmpty(forcedInvoiceDate)) {
            for (int i = 0; i < 10; i++)
                date.sendKeys(Keys.BACK_SPACE);
            date.sendKeys(forcedInvoiceDate);
        }
    }

    /**
     * clickNext
     */
    public void clickNext() {
        nextButton.click();
    }
    
}
