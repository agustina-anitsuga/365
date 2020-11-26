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
 * PencilPage
 * @author agustina.dagnino
 * 
 *
 */
public class PencilPage extends ProductPage {


    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PencilPage.class.getName());

    
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
    @FindBy(xpath = "//*[@id=\"tabular-buybox-truncate-1\"]/span[2]/span" )
    private WebElement seller7;
    private WebElement[] seller = { seller1, seller2, seller3, seller4, seller5, seller6, seller7 };

    @FindBy(xpath = "//*[@id=\"buyboxTabularTruncate-0\"]/span[2]/span")
    private WebElement distributor1;
    @FindBy(xpath = "//*[@id=\"buyboxTabularTruncate-0\"]/span[2]")
    private WebElement distributor2;
    @FindBy(xpath = "//*[@id=\"sfsb_accordion_head\"]/div[2]/div/span[1]" )
    private WebElement distributor3;
    @FindBy(xpath = "//*[@id=\"tabular-buybox-truncate-0\"]/span[2]/span" )
    private WebElement distributor4;
    private WebElement[] distributor = { distributor1, distributor2, distributor3, distributor4 };
  
    private Map<String,String> detailMap = null ;
    
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
    * MusicPage
    * @param driver
    */
    public PencilPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * go
     * @param url
     * @return
     */
    public PencilPage go(String url) {
        // go to url
        driver.get(url);

        // return page
        return new PencilPage(this.driver);
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

    /**
     * getDistributor
     * @return
     */
    public String getDistributor() {
        String ret = "";
        for (int i = 0; i < distributor.length; i++) {
            try {
                ret = distributor[i].getText();
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

    @Override
    public List<String> getEditionsUrls() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getTitle() {
        return title.getText();
    }

    public String getType() {
        String title = this.getTitle();
        return title.toLowerCase().contains("graphite") ? "Grafito" : "De color";
    }

    public String getBrand() {
        String ret = null;
        String[] keys = new String[] {"Marca", "Brand", "Fabricante", "Manufacturer"};
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

    public String getModel() {
        return "";
    }

    public String getUnitsPerPackage() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isWatercolor() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isPastel() {
        // TODO Auto-generated method stub
        return false;
    }

    public String getGrade() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
