package com.anitsuga.invoicer.page;

import org.openqa.selenium.By;
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

    //@FindBy( xpath = "//*[@id=\"detalle_descripcion1\"]" )
    //private WebElement product;

    //@FindBy( xpath = "//*[@id=\"detalle_precio1\"]" )
    //private WebElement price;
    
    //@FindBy( xpath = "//*[@id=\"detalle_tipo_iva1\"]" )
    //private WebElement iva;
    
    @FindBy( xpath = "//*[@id=\"detalles_datos\"]/input" )
    private WebElement newLineButton;
    
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
    public void setProduct(int line, String title) {
        WebElement product = driver.findElement(By.xpath("//*[@id=\"detalle_descripcion"+line+"\"]"));
        product.sendKeys(title);
    }

    /**
     * setPrice
     * @param aPrice
     */
    public void setPrice(int line, String aPrice) {
        WebElement price = driver.findElement(By.xpath("//*[@id=\"detalle_precio"+line+"\"]"));
        price.sendKeys(aPrice);
        price.sendKeys(Keys.TAB);
    }

    /**
     * setIva
     * @param ivaSelection
     */
    public void setIva(int line, String ivaSelection) {
        WebElement iva = driver.findElement(By.xpath("//*[@id=\"detalle_tipo_iva"+line+"\"]"));
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

    /**
     * newLine
     */
    public void newLine() {
        newLineButton.click(); 
    }

}
