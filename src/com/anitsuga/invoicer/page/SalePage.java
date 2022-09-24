package com.anitsuga.invoicer.page;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.anitsuga.fwk.page.Page;
import com.anitsuga.invoicer.model.Product;

/**
 * SalePage
 * @author agustina
 *
 */
public class SalePage extends Page {

    @FindBy( xpath = "//*[@class=\"sc_product\"]" )
    private WebElement product;

    @FindBy( xpath = "//*[@class=\"sc-title-subtitle-action__sublabel\"]/p" )
    private WebElement invoiceData; 
    
    @FindBy( xpath = "//*/div[@class=\"sc-buyer__content\"]/div/div/p" )
    private WebElement userData;
    
    @FindBy( xpath = "//*[@class=\"sc-account-rows__row__price\"]" )
    private WebElement price;
    
    @FindBy( xpath = "//*[@class=\"sc-notes\"]/div/button/span" )
    private WebElement addNoteButton;
    
    @FindBy( xpath = "//*[@class=\"sc-notes\"]/div[2]/div/label/div/input" )
    private WebElement noteTextField;
    
    @FindBy( xpath = "//*[@class=\"sc-notes\"]/div[2]/button/span" )
    private WebElement saveNoteButton;
    
    @FindBy( xpath = "//*[@class=\"sc-notes\"]/div[@class=\"sc-notes__content\"]/p[@class=\"sc-notes__content-text\"]" )
    private List<WebElement> existingNotes;
    
    @FindBy( xpath = "//*[@class=\"sc-product\"]" )
    private List<WebElement> productList;
    
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

    public String getTitle(){
        String ret = product.getText();
        return ret;
    }
    
    public List<Product> getProducts(){
        List<Product> products = new ArrayList<Product>();
        for (WebElement product : productList) {
            String aTitle = product.findElement(By.xpath("./div[@class=\"sc-title\"]")).getText();
            aTitle = aTitle.replace("Venta por publicidad", "");
            String aPrice = product.findElement(By.xpath("./div[@class=\"sc-price\"]")).getText();  
            String aQuantity = getQuantity(product);  
            aPrice = aPrice.replaceAll("\\.", "");
            aPrice = aPrice.replaceAll("\\$", "");
            aQuantity = aQuantity.replaceAll("u\\.", "");
            Product p = new Product(aTitle,aPrice,Integer.valueOf(aQuantity.trim()));
            products.add(p);
        }
        return products;
    }

    private String getQuantity(WebElement product) {
        String ret = null;
        try {
            ret = product.findElement(By.xpath("./div[@class=\"sc-quantity\"]")).getText();
        } catch (Exception e) {
            ret = product.findElement(By.xpath("./div[@class=\"sc-quantity sc-quantity__unique\"]")).getText();
        }
        return ret;
    }
    
    public String getTotalPrice(){
        String priceStr = price.getText();
        priceStr = priceStr.replaceAll("\\.", "");
        priceStr = priceStr.replaceAll("\\$", "");
        return priceStr.trim();
    }
    
    public String getCustomerDocType(){
        String docType = getCustomerDocTypeFromInvoiceData();
        return ((docType == "") || (docType == null)) ? getCustomerDocTypeFromUserData() : docType;
    }
    
    private String getCustomerDocTypeFromUserData() {
        String userDataStr = userData.getText();
        String ret = null;
        if ( userDataStr.indexOf("DNI") >= 0 )  ret = "DNI";
        if ( userDataStr.indexOf("CUIT") >= 0 )  ret = "CUIT";
        return ret;
    }

    private String getCustomerDocTypeFromInvoiceData() {
        String docType = null;
        try {
            String invoiceDataStr = invoiceData.getText();
            if( invoiceDataStr.contains("DNI") ){
                docType = "DNI";
            }
            if( invoiceDataStr.contains("CUIT") ){
                docType = "CUIT";
            }
        } catch (Exception e) {
            // do nothing
        }
        return docType;
    }
    
    public String getCustomerDocNumber(){
        String ret = getCustmerDocNumberFromInvoiceData();
        return ret == null? getCustomerDocNumberFromUserData():ret ;
    }

    private String getCustmerDocNumberFromInvoiceData() {
        String docNumber = null;
        try {
            String docType = this.getCustomerDocType();
            String invoiceDataStr = invoiceData.getText();
            int beginIndex = invoiceDataStr.indexOf(docType) + docType.length();
            int endIndex = invoiceDataStr.indexOf('\n');
            docNumber = invoiceDataStr.substring(beginIndex,endIndex).trim();
        } catch ( Exception e ) {
            // do nothing
        }
        return docNumber;
    }
    
    private String getCustomerDocNumberFromUserData() {
        String userDataStr = userData.getText();
        String parts[] = userDataStr.split("\\|");
        String ret = parts[1].replaceAll("DNI","");
        ret = ret.replaceAll("CUIT","");        
        return ret.trim();
    }
    
    public String getCustomerAddress(){
        String address = null;
        try {
            String invoiceDataStr = invoiceData.getText();
            int beginIndex = invoiceDataStr.indexOf('\n') +1;
            address = invoiceDataStr.substring(beginIndex);
        } catch (Exception e) {
            address = "";
        }
        return address;
    }
    
    public String getCustomerName(){
        String name = getCustomerNameFromInvoiceData();
        return (name==null)? getCustomerNameFromUserData(): name;
    }

    private String getCustomerNameFromUserData() {
        String userDataStr = userData.getText();
        String parts[] = userDataStr.split("\\|");
        String name = parts[0];
        name = name.replaceAll("<b>","");
        name = name.replaceAll("</b>","");
        return name.trim();
    }
    
    private String getCustomerNameFromInvoiceData() {
        String name = null;
        try {
            String invoiceDataStr = invoiceData.getText();
            int endIndex = invoiceDataStr.indexOf('-') ;
            name = invoiceDataStr.substring(0,endIndex-1);
        } catch (Exception e) {
            // do nothing
        }
        return name;
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
