package com.anitsuga.invoicer.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.anitsuga.fwk.page.Page;

/**
 * MenuPage
 * @author agustina
 *
 */
public class MenuPage extends Page {

    @FindBy( xpath = "//*[@id=\"btn_gen_cmp\"]/span[2]" )
    private WebElement generateInvoiceButton;
    

    /**
     * MenuPage
     * @param driver
     */
    public MenuPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * go
     * @param url
     * @return
     */
    public MenuPage go(String url) {
        // go to url
        driver.get(url);

        // return page
        return new MenuPage(this.driver);
    }

    public void clickGenerateInvoice(){
        generateInvoiceButton.click();
    }
}
