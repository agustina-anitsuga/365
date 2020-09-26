package com.anitsuga.invoicer.page;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.anitsuga.fwk.page.Page;

/**
 * SalePage
 * @author agustina
 *
 */
public class SalePage extends Page {

    @FindBy( xpath = "//*[@id=\"root-app\"]/div/div[1]/div/div[1]/h1" )
    private WebElement product;
    
    @FindBy( xpath = "//*[@id=\"root-app\"]/div/div[1]/div/div[4]/div/div/div[2]/div/p" )
    private WebElement invoiceData; 
    
    @FindBy( xpath = "//*[@id=\"root-app\"]/div/div[2]/div[1]/div[1]/div[2]/ul/li/div[2]/div" )
    private WebElement price;
    
    @FindBy( xpath = "//*[@id=\"root-app\"]/div/div[1]/div/div[6]/div[1]/button/span" )
    private WebElement addNoteButton;
    
    @FindBy( xpath = "//*[@id=\"root-app\"]/div/div[1]/div/div[6]/div[2]/label/div[1]/input" )
    private WebElement noteTextField;
    
    @FindBy( xpath = "//*[@id=\"root-app\"]/div/div[1]/div/div[6]/div[2]/button/span" )
    private WebElement saveNoteButton;
    
    @FindBy( xpath = "//*[@class=\"sc-notes\"]/div[@class=\"sc-notes__content\"]/p[@class=\"sc-notes__content-text\"]" )
    private List<WebElement> existingNotes;
    
    
    /**
     * PublicationPage
     * @param driver
     */
    public SalePage(WebDriver driver) {
        super(driver);
    }

    /**
     * go
     * @param url
     * @return
     */
    public SalePage go(String url) {
        // go to url
        driver.get(url);

        // return page
        return new SalePage(this.driver);
    }

    public String getProductTitle(){
        return product.getText();
    }
    
    public String getProductPrice(){
        String priceStr = price.getText();
        priceStr = priceStr.replaceAll("\\.", "");
        priceStr = priceStr.replaceAll("\\$", "");
        return priceStr.trim();
    }
    
    public String getCustomerDocType(){
        String invoiceDataStr = invoiceData.getText();
        String docType = null;
        if( invoiceDataStr.contains("DNI") ){
            docType = "DNI";
        }
        if( invoiceDataStr.contains("CUIT") ){
            docType = "CUIT";
        }
        return docType;
    }
    
    public String getCustomerDocNumber(){
        String docType = this.getCustomerDocType();
        String invoiceDataStr = invoiceData.getText();
        int beginIndex = invoiceDataStr.indexOf(docType) + docType.length();
        int endIndex = invoiceDataStr.indexOf('\n');
        String docNumber = invoiceDataStr.substring(beginIndex,endIndex);
        return docNumber.trim();
    }
    
    public String getCustomerAddress(){
        String invoiceDataStr = invoiceData.getText();
        int beginIndex = invoiceDataStr.indexOf('\n') +1;
        String address = invoiceDataStr.substring(beginIndex);
        return address;
    }
    
    public void addNote( String note ){
        addNoteButton.click();
        noteTextField.sendKeys(note);
        saveNoteButton.click();
    }

    public boolean includesInvoicedComment() {
        boolean ret = false;
        for (WebElement note : existingNotes) {
            ret |= note.getText().contains("F");
        }
        return ret;
    }
    
}
