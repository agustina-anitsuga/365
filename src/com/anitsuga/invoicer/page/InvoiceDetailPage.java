package com.anitsuga.invoicer.page;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import com.anitsuga.fwk.page.Page;

/**
 * InvoiceDetailPage
 * @author agustina
 *
 */
public class InvoiceDetailPage extends Page {

    @FindBy( xpath = "//*[@id=\"detalle_descripcion1\"]" )
    private WebElement product;

    @FindBy( xpath = "//*[@id=\"detalle_precio1\"]" )
    private WebElement price;
    
    @FindBy( xpath = "//*[@id=\"detalle_tipo_iva1\"]" )
    private WebElement iva;
    
    @FindBy( xpath = "//*[@id=\"contenido\"]/form/input[8]" )
    private WebElement nextButton;
    

    /**
     * InvoiceDetailPage
     * @param driver
     */
    public InvoiceDetailPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * setProduct
     * @param title
     */
    public void setProduct(String title) {
        product.sendKeys(title);
    }

    /**
     * setPrice
     * @param aPrice
     */
    public void setPrice(String aPrice) {
        price.sendKeys(aPrice);
        price.sendKeys(Keys.TAB);
    }

    /**
     * setIva
     * @param ivaSelection
     */
    public void setIva(String ivaSelection) {
        Select ivaSelect = new Select(iva);
        if( "Exento".equals(ivaSelection) ) {
            ivaSelect.selectByVisibleText(" Exento");
        } else {
            ivaSelect.selectByValue("5");
        }
    }

    /**
     * clickNext
     */
    public void clickNext() {
        nextButton.click();
    }

}
