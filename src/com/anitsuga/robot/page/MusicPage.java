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

import com.anitsuga.fwk.utils.SeleniumUtils;
import com.anitsuga.fwk.utils.StringUtils;

/**
 * MusicPage
 * @author agustina.dagnino
 * 
 *
 */
public class MusicPage extends ProductPage {


    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MusicPage.class.getName());

    @FindBy(xpath = "//*[@id=\"bylineInfo\"]/span[1]/a" )
    private WebElement artist1;
    @FindBy(xpath = "//*[@id=\"bylineInfo\"]/span/span[1]/a[1]" )
    private WebElement artist2;
    private WebElement[] artist = {artist1,artist2};
    
    @FindBy(xpath = "//*[@id=\"productTitle\"]")
    private WebElement title;
    
    @FindBy(xpath = "//*[@id=\"newBuyBoxPrice\"]" )
    private WebElement price1;
    @FindBy(xpath = "//*[@id=\"price_inside_buybox\"]")
    private WebElement price2;
    private WebElement[] price = { price1, price2 };
    
    @FindBy(xpath = "//*[@id=\"productDetailsTable\"]/tbody/tr/td/div/ul/li" )
    private List<WebElement> details1;
    @FindBy( xpath = "//*[@id=\"detailBullets_feature_div\"]/ul/li/span" )
    private List<WebElement> details2;
    
    @FindBy(xpath = "//*[@id=\"landingImage\"]" )
    private WebElement image;
    
    @FindBy(xpath = "//*[@id=\"availability\"]/span")
    private WebElement availability;

    @FindBy(xpath = "//*[@id=\"wayfinding-breadcrumbs_feature_div\"]/ul")
    private WebElement type;
    
    @FindBy(xpath = "//*[@id=\"landingImage\"]")    
    private WebElement photoViewer1;
   
    @FindBy(xpath = "//*[@id=\"ivThumbs\"]/div[@class=\"ivRow\"]/div")
    private List<WebElement> photoMiniatures;
  

    @FindBy(xpath = "//*[@id=\"ivLargeImage\"]/img")
    private WebElement photoTarget1;
    @FindBy(xpath = "//*[@id=\"landingImage\"]")
    private WebElement photoTarget2;

    @FindBy(xpath = "//*[@id=\"merchant-info\"]")
    private WebElement seller1;
    @FindBy(xpath = "//*[@id=\"buyNewInner\"]/div[@class=\"a-section a-spacing-small\"]/span")
    private WebElement seller2;
    @FindBy(xpath = "//*[@id=\"newOfferAccordionRow\"]/div/div[2]/div/div/div[1]/div/span")
    private WebElement seller3;
    @FindBy(xpath = "//*[@id=\"buyboxTabularTruncate-1\"]/span[2]/span")
    private WebElement seller4;
    @FindBy(xpath = "//*[@id=\"buyboxTabularTruncate-1\"]/span[2]")
    private WebElement seller5;
    @FindBy(xpath = "//*[@id=\"sfsb_accordion_head\"]/div[2]/div/span[2]" )
    private WebElement seller6;
    private WebElement[] seller = { seller1, seller2, seller3, seller4, seller5, seller6 };
    
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

    @FindBy(xpath = "//*[@id=\"bylineInfo\"]/span[3]" )
    private WebElement albumFormat1;
    @FindBy(xpath = "//*[@id=\"bylineInfo\"]/span[4]" )
    private WebElement albumFormat2;
    @FindBy(xpath = "//*[@id=\"bylineInfo\"]/span[5]" )
    private WebElement albumFormat3;
    @FindBy(xpath = "//*[@id=\"bylineInfo\"]/span[6]" )
    private WebElement albumFormat4;
    @FindBy(xpath = "//li[@class=\"swatchElement selected resizedSwatchElement\"]/span/span/span/a/span" )
    private WebElement albumFormat5;
    private WebElement[] albumFormat = { albumFormat1, albumFormat2, albumFormat3, albumFormat4, albumFormat5 };


   /**
    * MusicPage
    * @param driver
    */
    public MusicPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * go
     * @param url
     * @return
     */
    public MusicPage go(String url) {
        // go to url
        driver.get(url);

        // return page
        return new MusicPage(this.driver);
    }
    
    /**
     * getGenre
     * @return
     */
    public String getGenre() {
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
     * waitForPopups
     */
    public void waitForPopups() {
        String[] buttons = new String[] {
                "//*[@id=\"a-popover-1\"]/div/header/button",
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
                if(ret.indexOf(";")>0)
                ret = ret.substring(1,ret.indexOf(";"));
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
        String[] keys = new String[] {"Dimensiones del producto"};
        Map<String,String> details = getDetailMap();
        
        for (int i = 0; i < keys.length; i++) {
            String weight = (String) details.get(keys[i]);
            if( weight!=null ){
                if(weight.indexOf(";") > 0){
                    ret = weight.substring(weight.indexOf(";")+1,weight.length()).trim();
                } 
                break;
            }
        }
        return (ret==null)? "" : ret.trim();
    }
    
    /**
     * getAlbum
     * @return
     */
    public String getAlbum() {
        String ret = title.getText();
        ret = ret.replace("[Vinyl]", "");
        ret = ret.replace("(Vinyl)", "");
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
            LOGGER.error("Could not open photo viewer");
        }
    }
        
    /**
     * getImages
     * @return
     */
    public List<String> getImages(){
        List<String> ret = new ArrayList<String>();
        for (WebElement photoMiniature : photoMiniatures) {
            if( !photoMiniature.isSelected() && photoMiniature.isDisplayed() ){
                photoMiniature.click();
            }
            sleep(500);
            try{

                ret.add(getPhotoUrl(photoTarget1));
            } catch (Exception e) {
                ret.add(getPhotoUrl(photoTarget2));
            }
        }
        return ret;
    }

    /**
     * sleep
     */
    private void sleep(int millis) {
        try {
        Thread.sleep(500);
        } catch (Exception e) {}
    }

    /**
     * getPhotoUrl
     * @param photoTarget
     * @return
     */
    private String getPhotoUrl(WebElement photoTarget) {
        String photo = null;
        do {
            photo = photoTarget1.getAttribute("src");
            sleep(100);
        } while (photo.contains("loadIndicators"));
        return photo;
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
        return "Fisico";
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
        return text.startsWith("CD de audio")
                   ||text.startsWith("Vinilos");
    }

    /**
     * getEditionsUrls
     * @return
     */
    public List<String> getEditionsUrls() {
        List<String> ret = new ArrayList<String>();
        try {
            LOGGER.info("Editions... ");
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

    /**
     * getAlbumFormat
     * @return
     */
    public String getAlbumFormat() {
        String ret = "";
        for (int i = 0; i < albumFormat.length; i++) {
            try {
                String format = albumFormat[i].getText();
                ret = translateFormat(format);
                if( !StringUtils.isEmpty(ret) ){
                    break;
                }
            } catch (Exception e) {
                LOGGER.error("Error reading album format");
            }

        }
        return ret;
    }

    /**
     * translateFormat
     * @param format
     * @return
     */
    private String translateFormat(String format) {
        String ret = "";
        if( "Vinyl".equals(format) ){
            ret = "Vinilo";
        }
        if( "CD de audio".equals(format) ){
            ret = "CD";
        }
        if( "Audio CD".equals(format) ){
            ret = "CD";
        }
        return ret;
    }

    /**
     * hasAdditionalTracks
     * @return
     */
    public boolean hasAdditionalTracks() {
        return false;
    }

    /**
     * getReleaseYear
     * @return
     */
    public String getReleaseYear() {
        String ret = null;
        String[] keys = new String[] {"Fecha de lanzamiento original"};
        Map<String,String> details = getDetailMap();
        
        for (int i = 0; i < keys.length; i++) {
            String releaseYear = (String) details.get(keys[i]);
            if( releaseYear!=null ){
                ret = releaseYear; 
                break;
            }
        }
        return (ret==null)? "1900" : ret.trim();
    }

    /**
     * getNumberOfDisks
     * @return
     */
    public String getNumberOfDisks() {
        String ret = null;
        String[] keys = new String[] {"Número de discos"};
        Map<String,String> details = getDetailMap();
        
        for (int i = 0; i < keys.length; i++) {
            String numberOfDisks = (String) details.get(keys[i]);
            if( numberOfDisks!=null ){
                ret = numberOfDisks; 
                break;
            }
        }
        return (ret==null)? "1" : ret.trim();
    }

    /**
     * getNumberOfSongs
     * @return
     */
    public String getNumberOfSongs() {
        return "1";
    }

    /**
     * getOrigin
     * @return
     */
    public String getOrigin() {
        return "Estados Unidos";
    }

    /**
     * getArtist
     * @return
     */
    public String getArtist() {
        //return artist.getText();
        String ret = "";
        for (int i = 0; i < artist.length; i++) {
            try {
                ret = artist[i].getText();
                if( !StringUtils.isEmpty(ret) ){
                    break;
                }
            } catch (Exception e) {
                LOGGER.error("Error reading artist");
            }
        }
        return ret;
    }

    /**
     * getAmazonID
     * @return
     */
    public String getAmazonID() {
        String ret = null;
        String[] keys = new String[] {"ASIN"};
        Map<String,String> details = getDetailMap();
        
        for (int i = 0; i < keys.length; i++) {
            String amazonID = (String) details.get(keys[i]);
            if( amazonID!=null ){
                ret = amazonID; 
                break;
            }
        }
        return (ret==null)? "" : ret.trim();
    }
    
}
