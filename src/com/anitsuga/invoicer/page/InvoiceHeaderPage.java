package com.anitsuga.invoicer.page;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import com.anitsuga.fwk.page.Page;

/**
 * InvoiceHeaderPage
 * @author agustina
 *
 */
public class InvoiceHeaderPage extends Page {

    @FindBy( xpath = "//*[@id=\"idivareceptor\"]" )
    private WebElement customerType;
    
    @FindBy( xpath = "//*[@id=\"idtipodocreceptor\"]" )
    private WebElement customerDocType;
    
    @FindBy( xpath = "//*[@id=\"nrodocreceptor\"]" )
    private WebElement customerDocNumber;
    
    @FindBy( xpath = "//*[@id=\"domicilioreceptor\"]" )
    private WebElement customerAddress;
    
    @FindBy( xpath = "//*[@id=\"formadepago7\"]" )
    private WebElement paymentType;
    
    @FindBy( xpath = "//*[@id=\"formulario\"]/input[2]")
    private WebElement nextButton;
    
    @FindBy( xpath = "//*[@id=\"razonsocialreceptor\"]")
    private WebElement customerName;
    
    
    /**
     * InvoiceHeaderPage
     * @param driver
     */
    public InvoiceHeaderPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * getCustomerName
     * @return customer name as known by invoicing service
     * @throws InterruptedException 
     */
    public String getCustomerName() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String ret = customerName.getAttribute("value");
        return ret;
    }
    
    /**
     * setDefaultCustomerType
     */
    public void setDefaultCustomerType() {
        Select customerTypeSelect = new Select(customerType);
        customerTypeSelect.selectByVisibleText(" Consumidor Final");
    }

    /**
     * setCustomerTypeResponsableInscripto
     */
    public void setCustomerTypeResponsableInscripto() {
        Select customerTypeSelect = new Select(customerType);
        customerTypeSelect.selectByVisibleText(" IVA Responsable Inscripto");
    }

    /**
     * setCustomerDocType
     * @param docType
     */
    public void setCustomerDocType(String docType) {
        Select customerDocTypeSelect = new Select(customerDocType);
        customerDocTypeSelect.selectByVisibleText(docType);   
    }

    /**
     * setCustomerDocNumber
     * @param docNumber
     */
    public void setCustomerDocNumber(String docNumber) {
        customerDocNumber.sendKeys(docNumber);
        customerDocNumber.sendKeys(Keys.TAB);
    }

    /**
     * setCustomerAddress
     * @param address
     */
    public void setCustomerAddress(String address) {
        customerAddress.sendKeys(address);
    }

    /**
     * setDefaultPaymentType
     */
    public void setDefaultPaymentType() {
        if( !paymentType.isSelected() )
            paymentType.click();
    }

    /**
     * clickNext
     */
    public void clickNext() {
        nextButton.click();
    }


}
