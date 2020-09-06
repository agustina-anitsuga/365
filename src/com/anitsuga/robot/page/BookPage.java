package com.anitsuga.robot.page;

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

import com.anitsuga.fwk.page.Page;
import com.anitsuga.fwk.utils.SeleniumUtils;
import com.anitsuga.fwk.utils.StringUtils;

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
    private WebElement author1;
    @FindBy(xpath = "//*[@id=\"bylineInfo\"]/span/a")
    private WebElement author2;
    private WebElement[] author = { author1, author2 };
    
    @FindBy(xpath = "//*[@id=\"buyNewSection\"]/a/h5/div/div[2]/div/span[2]" )
    private WebElement price1;
    @FindBy(xpath = "//*[@id=\"buyNewSection\"]/h5/div/div[2]/div/span[2]")
    private WebElement price2;
    @FindBy(xpath = "//*[@id=\"mediaNoAccordion\"]/div[1]/div[2]/span[2]" )
    private WebElement price3;
    @FindBy(xpath = "//*[@id=\"newOfferAccordionRow\"]/div/div[1]/a/h5/div/div[2]/span[2]")
    private WebElement price4;
    @FindBy(xpath = "//*[@id=\"buyBoxInner\"]/div/div[1]/ul/li[1]/span/span[2]")
    private WebElement price5;    
    @FindBy(xpath = "//*[@id=\"newBuyBoxPrice\"]")
    private WebElement price6;    
    @FindBy(xpath = "//*[@id=\"price\"]")
    private WebElement price7;
    private WebElement[] price = { price1, price2, price3, price4, price5, price6, price7 };
    
    @FindBy(xpath = "//*[@id=\"productDetailsTable\"]/tbody/tr/td/div/ul/li" )
    private List<WebElement> details1;
    @FindBy( xpath = "//*[@id=\"detailBullets_feature_div\"]/ul/li/span" )
    private List<WebElement> details2;
    
    @FindBy(xpath = "//*[@id=\"imgBlkFront\"]" )
    private WebElement image;
    
    @FindBy(xpath = "//*[@id=\"availability\"]/span")
    private WebElement availability;
    
    @FindBy(xpath = "//*[@id=\"wayfinding-breadcrumbs_feature_div\"]/ul")
    private WebElement type;

    @FindBy(xpath = "//*[@id=\"imgThumbs\"]/span/span")    
    private WebElement photoViewer1;
    @FindBy(xpath = "//*[@id=\"imgBlkFront\"]")    
    private WebElement photoViewer2;
    
    @FindBy(xpath = "//*[@id=\"imgGalleryContent\"]/div[2]/div/img")
    private List<WebElement> photoMiniatures;
    
    @FindBy(xpath = "//*[@id=\"igImage\"]")
    private WebElement photoTarget1;
    @FindBy(xpath = "//*[@id=\"sitbReaderPage-1\"]/img")
    private WebElement photoTarget2;

    @FindBy(xpath = "//*[@id=\"merchant-info\"]")
    private WebElement seller1;
    @FindBy(xpath = "//*[@id=\"buyNewInner\"]/div[@class=\"a-section a-spacing-small\"]/span")
    private WebElement seller2;
    @FindBy(xpath = "//*[@id=\"newOfferAccordionRow\"]/div/div[2]/div/div/div[1]/div/span")
    private WebElement seller3;
    @FindBy(xpath = "//*[@id=\"buyboxTabularTruncate-1\"]/span[2]/span")
    private WebElement seller4;
    private WebElement[] seller = { seller1, seller2, seller3, seller4 };
    
    private Map<String,String> detailMap = null ;
    
    @FindBy(xpath = "//*[@class=\"a-button-text\"]")
    private List<WebElement> editions;

    @FindBy(xpath = "//*[@id=\"formats\"]")
    private WebElement formats;

    @FindBy(xpath = "//li[@class=\"swatchElement selected resizedSwatchElement\"]")
    private WebElement sellerList1;
    @FindBy(xpath = "//li[@class=\"swatchElement selected\"]")
    private WebElement sellerList2;
    @FindBy(xpath = "//*[@id=\"mediaOlp\"]/div/div/div/div[1]")
    private WebElement sellerList3;
    private WebElement[] sellerList = { sellerList1, sellerList1, sellerList2, sellerList2 };

    private String[] sellerListAddin = new String[] {
            ".//span[@class=\"olp-new olp-link\"]/a",
            ".//span[@class=\"olp-new olp-link\"]/span/a",
            ".//span[@class=\"olp-new olp-link\"]/a",
            ".//span[@class=\"olp-new olp-link\"]/span/a",
            };
   
    
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
            if( firstLevel >= 0 ){
                int end = ret.indexOf(">",firstLevel+1);
                ret = ret.substring(firstLevel+1,(end>=0)?end:ret.length());
                ret = ret.trim();
            }
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
        String keys[] = new String[]{"Idioma","Language","Idioma:"};
        String lang = null;
        Map<String,String> details = getDetailMap();
        for (int i = 0; i < keys.length; i++) {
            lang = (String) details.get(keys[i]);
            if(!StringUtils.isEmpty(lang)){
                break;
            }
        }
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
            for (WebElement detail : details1) {
                 String content = detail.getText();
                 if(content.contains(":")){
                     String key = (content.substring(0,content.indexOf(":")).trim());
                     String value = (content.substring(content.indexOf(":")+1,content.length())).trim();
                     detailMap.put(key, value);
                 } else {
                     detailMap.put(content, "");
                 }
            }
            for (WebElement detail : details2) {
                String key = detail.findElement(By.xpath("./span[1]")).getText();
                String value = detail.findElement(By.xpath("./span[2]")).getText();
                if( key.endsWith(":") ) { key = key.substring(0,key.length()-1); };
                detailMap.put(key.trim(), value.trim());
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
        String[] keys = new String[] {"Editor","Publisher","Editorial"};
        Map<String,String> details = getDetailMap();
        String ret = null;
        for (int i = 0; i < keys.length; i++) {
            ret = (String) details.get(keys[i]);    
            if( !StringUtils.isEmpty(ret) ){
                if( ret.indexOf(";") >0 ){
                    ret = ret.substring(0,ret.indexOf(";"));
                }
                if( ret.indexOf("(") >0 ){
                    ret = ret.substring(0,ret.indexOf("("));
                }
                break;
            }
        }
        return (ret==null)? "": ret.trim();
    }

    /**
     * getDimensions
     * @return
     */
    public String getDimensions() {
        String[] keys = new String[]{"Dimensiones del producto", "Product Dimensions"};
        Map<String,String> details = getDetailMap();
        String ret = null;
        for (int i = 0; i < keys.length; i++) {
            ret = (String) details.get(keys[i]);    
            if(!StringUtils.isEmpty(ret)){
                break;
            }
        }
        return (ret!=null)?ret.trim():ret;
    }
    
    /**
     * getAvailability
     * @return
     */
    public String getAvailability(){
        String ret = "Unable to retrieve availability";
        try {
            ret = availability.getText();
        } catch (Exception e) {
            LOGGER.error("Unable to retrieve availability");
        }
        return ret;
    }
    
    /**
     * getWeight
     * @return
     */
    public String getWeight() {
        String ret = null;
        String[] keys = new String[] {"Peso del envío","Shipping Weight", "Peso del producto"};
        Map<String,String> details = getDetailMap();
        
        for (int i = 0; i < keys.length; i++) {
            String weight = (String) details.get(keys[i]);
            if( weight!=null ){
                if(weight.indexOf("(") > 0){
                    ret = weight.substring(0, weight.indexOf("("));
                } else {
                    ret = weight;
                }
                break;
            }
        }
        return (ret==null)? "" : ret.trim();
    }
    
    /**
     * getCover
     * @return
     */
    public String getCover() {
        String ret = "";
        Map<String,String> details = getDetailMap();
        String keysForHardcover[] = new String[]{"Tapa dura","Hardcover","Library Binding"};
        String keysForPaperback[] = new String[]{"Paperback","Mass Market Paperback","Libro de bolsillo", "Tapa blanda"};
        
        for (int i = 0; i < keysForHardcover.length; i++) {
            String isHardcover = (String) details.get(keysForHardcover[i]);
            if( isHardcover!=null ){
                ret = "Dura";
            }
        }
        
        for (int i = 0; i < keysForPaperback.length; i++) {
            String isPaperback = (String) details.get(keysForPaperback[i]);
            if( isPaperback!=null ){
                ret = "Blanda";
            }
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
        String[] keys = {"Hardcover","Paperback","Library Binding","Board book","Mass Market Paperback","Tapa dura", "Tapa blanda", "Libro de bolsillo"};
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
        for (int i = 0; i < author.length; i++) {
            try {
                ret = author[i].getText();
            } catch (Exception e) {
                //
            }
            if( !StringUtils.isEmpty(ret) ){
                break;
            }
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
        for (int i = 0; i < price.length; i++) {
            try {
                ret = price[i].getText();
            } catch (Exception e) {
                //
            }
            if( !StringUtils.isEmpty(ret) ){
                break;
            }
        }
        return ret;
    }
    
    /**
     * openPhotoViewer
     */
    public void openPhotoViewer(){
        try {
            photoViewer1.click();
        } catch (Exception e) {
            photoViewer2.click();
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
            ret.add(photoTarget1.getAttribute("src"));
        }
        return ret;
    }
    
    /**
     * getSeller
     * @return
     */
    public String getSeller(){
        String ret = "";
        for (int i = 0; i < seller.length; i++) {
            try {
                ret = seller[i].getText();
                ret = ret.trim();
            } catch (Exception e) {
                //
            }
            if( !StringUtils.isEmpty(ret) ){
                break;
            }
        }
        return ret;
    }
    
    /**
     * getFormat
     * @return
     */
    public String getFormat(){
        return "Papel";
    }

    /**
     * getEditions
     * @return
     */
    private List<WebElement> getEditions() {
        List<WebElement> ret = new ArrayList<WebElement>();
        try {
            for (int i = 0; i < 20; i++) {
                String xpath = "//*[@id=\"a-autoid-"+(i+1)+"-announce\"]";
                List<WebElement> elements = formats.findElements(By.xpath(xpath));
                for (WebElement webElement : elements) {
                    String text = webElement.getText();
                    if(editionIsOfInterest(text)){
                        ret.add(webElement);
                        break;
                    }
                }
                xpath = "//*[@id=\"mediaTab_heading_"+i+"\"]";
                elements = formats.findElements(By.xpath(xpath));
                for (WebElement webElement : elements) {
                    String text = webElement.getText();
                    if(editionIsOfInterest(text)){
                        WebElement elem = webElement.findElement(By.xpath("a"));
                        ret.add(elem);
                        break;
                    }
                }
            }
        } catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return ret;
    }

    /**
     * editionIsOfInterest
     * @param text
     * @return
     */
    private boolean editionIsOfInterest(String text) {
        return text.startsWith("Pasta dura")
                   ||text.startsWith("Tapa dura")
                   ||text.startsWith("Encuadernación de biblioteca")
                   ||text.startsWith("Pasta blanda")
                   ||text.startsWith("Tapa flexible")
                   ||text.startsWith("Libro de bolsillo");
    }

    /**
     * getEditionsUrls
     * @return
     */
    public List<String> getEditionsUrls() {
        List<String> ret = new ArrayList<String>();
        try {
            LOGGER.info("Editions...");
            List<WebElement> webElements = this.getEditions();
            for (WebElement webElement : webElements) {
                String url = webElement.getAttribute("href");
                ret.add(url);
                LOGGER.info("    "+url);
            }
            
        } catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return ret;
    }

    /**
     * getSellerListUrl
     * @return
     */
    public String getSellerListUrl() {
        String url = null;
        for (int i = 0; i < sellerList.length; i++) {
            try {
                WebElement sellerListUrl = sellerList[i].findElement(By.xpath(sellerListAddin[i]));
                url = sellerListUrl.getAttribute("href");
            } catch(Exception e){
                //LOGGER.error(e.getMessage());
            }
            if( !StringUtils.isEmpty(url) ){
                break;
            }
        }
        
        try {
            if(StringUtils.isEmpty(url)){
                List<WebElement> sellerListUrls = sellerList3.findElements(By.xpath(".//span/a"));
                for (WebElement webElement : sellerListUrls) {
                    if( webElement.getText().contains("Nuevo") ){
                        url = webElement.getAttribute("href");
                    }
                }
            }
        } catch (Exception e1) {
            LOGGER.error(e1.getMessage());
        }
        return url;
    }
    
}
