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

    @FindBy( xpath = "//*[@id=\"contenido\"]/form/input[2]")
    private WebElement nextButton2;

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
     * setQuantity
     * @param line
     * @param quantity
     */
    public void setQuantity(int line, int quantity) {
        WebElement qty = driver.findElement(By.xpath("//*[@id=\"detalle_cantidad"+line+"\"]"));
        qty.sendKeys(Keys.BACK_SPACE);
        qty.sendKeys(String.valueOf(quantity));
        qty.sendKeys(Keys.TAB);
    }

    public void setDefaultUnits(int line) {
        WebElement units = driver.findElement(By.xpath("//*[@id=\"detalle_medida"+line+"\"]"));
        Select unitsSelect = new Select(units);
        unitsSelect.selectByVisibleText(" unidades");
    }
    
    /**
     * clickNext
     */
    public void clickNext() {
        nextButton.click();
    }

    /**
     * clickNext
     */
    public void clickNext2() {
        nextButton2.click();
    }

    /**
     * newLine
     */
    public void newLine() {
        newLineButton.click(); 
    }

}
