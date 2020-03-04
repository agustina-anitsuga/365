package com.robot.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.robot.utils.SeleniumUtils;
import com.robot.utils.StringUtils;

/**
 * BookPage
 * @author agustina.dagnino
 * 
 *
 */
public class BookPage extends Page {


    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BookPage.class.getName());
    
    

    @FindBy(xpath = "//*[@id=\"productTitle\"]")
    private WebElement title;

    @FindBy(xpath = "//*[@id=\"bylineInfo\"]/span/span[1]/a[1]")
    private WebElement author;

    @FindBy(xpath = "//*[@id=\"bylineInfo\"]/span/a")
    private WebElement author1;

    @FindBy(xpath = "//*[@id=\"buyNewSection\"]/a/h5/div/div[2]/div/span[2]" )
    private WebElement price;
    
    @FindBy(xpath = "//*[@id=\"buyNewSection\"]/h5/div/div[2]/div/span[2]")
    private WebElement price1;

    @FindBy(xpath = "//*[@id=\"mediaNoAccordion\"]/div[1]/div[2]/span[2]" )
    private WebElement price2;
  
    @FindBy(xpath = "//*[@id=\"newOfferAccordionRow\"]/div/div[1]/a/h5/div/div[2]/span[2]")
    private WebElement price3;

    @FindBy(xpath = "//*[@id=\"buyBoxInner\"]/div/div[1]/ul/li[1]/span/span[2]")
    private WebElement price4 ;
    
    @FindBy(xpath = "//*[@id=\"productDetailsTable\"]/tbody/tr/td/div/ul/li" )
    private List<WebElement> details;
    
    @FindBy(xpath = "//*[@id=\"imgBlkFront\"]" )
    private WebElement image;
    
    @FindBy(xpath = "//*[@id=\"availability\"]/span")
    private WebElement availability;
    
    @FindBy(xpath = "//*[@id=\"wayfinding-breadcrumbs_feature_div\"]/ul")
    private WebElement type;

    @FindBy(xpath = "//*[@id=\"imgThumbs\"]/span/span")    
    private WebElement photoViewer;

    @FindBy(xpath = "//*[@id=\"imgBlkFront\"]")    
    private WebElement photoViewer1;
    
    @FindBy(xpath = "//*[@id=\"imgGalleryContent\"]/div[2]/div/img")
    private List<WebElement> photoMiniatures;
    
    @FindBy(xpath = "//*[@id=\"igImage\"]")
    private WebElement photoTarget;
    
    @FindBy(xpath = "//*[@id=\"sitbReaderPage-1\"]/img")
    private WebElement photoTarget1;
    
    @FindBy(xpath = "//*[@id=\"merchant-info\"]")
    private WebElement seller;
    
    // map of book details
    private Map<String,String> detailMap = null ;
    
    
   /**
    * BookPage
    * @param driver
    */
    public BookPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * go
     * @param url
     * @return
     */
    public BookPage go(String url) {
        // go to url
        driver.get(url);

        // return page
        return new BookPage(this.driver);
    }
    
    /**
     * getType
     * @return
     */
    public String getType() {
        String ret = "";
        try {
            ret = type.getText();
            ret = ret.replaceAll("\n", " ");
            ret = ret.replaceAll("›", ">");
            int firstLevel = ret.indexOf(">");
            int end = ret.indexOf(">",firstLevel+1);
            ret = ret.substring(firstLevel+1,(end>=0)?end:ret.length());
            ret = ret.trim();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return ret;
    }

    /**
     * getLanguage
     * @return
     */
    public String getLanguage() {
        String lang = null;
        Map<String,String> details = getDetailMap();
        lang = (String) details.get("Idioma");
        Map<String, String> translations = getLanguageTranslations();
        lang = translations.getOrDefault(lang, lang);
        return lang;
    }

    /**
     * getLanguageTranslations
     * @return
     */
    private Map<String, String> getLanguageTranslations() {
        Map<String,String> translations = new HashMap<String,String>();
        translations.put("English","Inglés");
        translations.put("Spanish","Español");
        translations.put("Portuguese","Portugués");
        return translations;
    }

    /**
     * waitForPopups
     */
    public void waitForPopups() {
        String[] buttons = new String[] {
                "//*[@id=\"a-popover-2\"]/div/header/button",
                "//*[@id=\"a-popover-3\"]/div/header/button",
                "//*[@id=\"a-popover-1\"]/div/header/button",
                "//*[@id=\"a-popover-4\"]/div/header/button",
                "//*[@id=\"nav-main\"]/div[1]/div[2]/div/div[3]/span[1]/span/input"};
        
        for (int i = 0; i < buttons.length; i++) {
            try {
                String dismissButtonXPath = buttons[i];
                WebDriverWait wait = SeleniumUtils.getWait(driver);
                wait.until(ExpectedConditions.presenceOfElementLocated( 
                        By.xpath(dismissButtonXPath) ));
                WebElement element = driver.findElement(By.xpath(dismissButtonXPath));
                element.click();
            } catch ( Exception e ){
                LOGGER.debug(e.getMessage());
            }
        }
    }

    /**
     * getDetailMap
     * @return map with book details
     */
    private Map<String,String> getDetailMap() {
        if(detailMap==null){
            detailMap = new HashMap<String,String>();
            for (WebElement detail : details) {
                 String content = detail.getText();
                 if(content.contains(":")){
                     String key = (content.substring(0,content.indexOf(":")).trim());
                     String value = (content.substring(content.indexOf(":")+1,content.length())).trim();
                     detailMap.put(key, value);
                 } else {
                     detailMap.put(content, "");
                 }
            }
        }
        return detailMap;
    }
    
    /**
     * getImage
     * @return
     */
    public String getImage() {
        String ret = image.getAttribute("src");
        return ret;
    }
    
    /**
     * getISBN
     * @return ISBN-13 or ISBN-10
     */
    public String getISBN() {
        String isbn = null;
        Map<String,String> details = getDetailMap();
        isbn = (String) details.get("ISBN-13");
        if( isbn==null ){
            isbn = (String) details.get("ISBN-10");
        }
        return isbn;
    }

    /**
     * getEditorial
     * @return
     */
    public String getEditorial() {
        Map<String,String> details = getDetailMap();
        String ret = (String) details.get("Editor");    
        if( ret.indexOf(";") >0 ){
            ret = ret.substring(0,ret.indexOf(";"));
        }
        if( ret.indexOf("(") >0 ){
            ret = ret.substring(0,ret.indexOf("("));
        }
        return ret.trim();
    }

    /**
     * getDimensions
     * @return
     */
    public String getDimensions() {
        Map<String,String> details = getDetailMap();
        String ret = (String) details.get("Dimensiones del producto");    
        return (ret!=null)?ret.trim():ret;
    }
    
    /**
     * getAvailability
     * @return
     */
    public String getAvailability(){
        String ret = availability.getText();
        return ret;
    }
    
    /**
     * getWeight
     * @return
     */
    public String getWeight() {
        String ret = null;
        Map<String,String> details = getDetailMap();
        String weight = (String) details.get("Peso del envío");
        if( weight!=null ){
            if(weight.indexOf("(") > 0){
                ret = weight.substring(0, weight.indexOf("("));
            } else {
                ret = weight;
            }
        }
        return ret.trim();
    }
    
    /**
     * getCover
     * @return
     */
    public String getCover() {
        String ret = "";
        Map<String,String> details = getDetailMap();
        String isHardcover = (String) details.get("Hardcover");
        if( isHardcover!=null ){
            ret = "Dura";
        } 
        String isPaperback = (String) details.get("Paperback");
        if( isPaperback!=null ){
            ret = "Blanda";
        }
        return ret;
    }
    
    /**
     * getCoverFullData
     * @return
     */
    public String getCoverFullData() {
        String ret = "";
        Map<String,String> details = getDetailMap();
        String[] keys = {"Hardcover","Paperback","Board book","Mass Market Paperback"};
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            String value = (String) details.get(key);
            if( !StringUtils.isEmpty(value) ){
                ret = key + " " + value ;
                break;
            } 
        }
        return ret;
    }

    /**
     * getAuthor
     * @return
     */
    public String getAuthor() {
        String ret = null;
        try {
            ret = author.getText();
        } catch (Exception e) {
            ret = author1.getText();
        }
        return ret;
    }

    /**
     * getTitle
     * @return
     */
    public String getTitle() {
        String ret = title.getText();
        return ret;
    }

    /**
     * getPrice
     * @return
     */
    public String getPrice() {
        String ret = null;
        try { 
            ret = price.getText();
        } catch (Exception e) {
            try {
                ret = price1.getText();
            } catch (Exception e1) {
                try{
                    ret = price2.getText();
                } catch (Exception e2) {
                    try {
                        ret = price3.getText();
                    } catch (Exception e3) {
                        try {
                            ret = price4.getText();
                        } catch (Exception e4) {
                            ret = "";
                        }
                    }
                }
            }
        }
        return ret;
    }
    
    /**
     * openPhotoViewer
     */
    public void openPhotoViewer(){
        try {
            photoViewer.click();
        } catch (Exception e) {
            photoViewer1.click();
        }
    }
    
    /**
     * getImages
     * @return
     */
    public List<String> getImages(){
        List<String> ret = new ArrayList<String>();
        for (WebElement photoMiniature : photoMiniatures) {
            if( !photoMiniature.isSelected() ){
                photoMiniature.click();
            }
            ret.add(photoTarget.getAttribute("src"));
        }
        /*
        if(ret.size()==0){
            try {
                ret.add(photoTarget1.getAttribute("src"));                
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
        */
        return ret;
    }
    
    /**
     * getSeller
     * @return
     */
    public String getSeller(){
        String ret = seller.getText();
        return ret.trim();
    }
    
    /**
     * getFormat
     * @return
     */
    public String getFormat(){
        return "Papel";
    }
}
