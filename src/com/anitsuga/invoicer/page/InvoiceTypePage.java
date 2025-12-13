package com.anitsuga.invoicer.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import com.anitsuga.fwk.page.Page;

/**
 * InvoiceTypePage
 * @author agustina
 *
 */
public class InvoiceTypePage extends Page {

    
    @FindBy( xpath = "//*[@id=\"puntodeventa\"]" )
    private WebElement salesPoint;
    
    @FindBy( xpath = "//*[@id=\"universocomprobante\"]" )
    private WebElement invoiceType;
    
    @FindBy( xpath = "//*[@id=\"contenido\"]/form/input[2]")
    private WebElement nextButton;
    
    

    /**
     * InvoiceTypePage
     * @param driver
     */
    public InvoiceTypePage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * selectDefaultSalesPoint
     */
    public void selectDefaultSalesPoint(){
        Select salesPointSelect = new Select(salesPoint);
        salesPointSelect.selectByIndex(1);
    }

    /**
     * selecteDefaultInvoiceType
     */
    public void selectDefaultInvoiceType(){
        Select invoiceTypeSelect = new Select(invoiceType);
        invoiceTypeSelect.selectByVisibleText("Factura B");
        //invoiceTypeSelect.selectByValue("2");
    }

    public void selectInvoiceTypeA() {
        Select invoiceTypeSelect = new Select(invoiceType);
        invoiceTypeSelect.selectByVisibleText("Factura A");
    }

    /**
     * clickNext
     */
    public void clickNext(){
        nextButton.click();
    }

}
