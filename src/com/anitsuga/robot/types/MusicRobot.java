package com.anitsuga.robot.types;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anitsuga.fwk.page.LoginPage;
import com.anitsuga.fwk.utils.AppProperties;
import com.anitsuga.fwk.utils.SeleniumUtils;
import com.anitsuga.fwk.utils.StringUtils;
import com.anitsuga.robot.Robot;
import com.anitsuga.robot.model.Music;
import com.anitsuga.robot.model.Publication;
import com.anitsuga.robot.model.SellerQuote;
import com.anitsuga.robot.page.MusicPage;
import com.anitsuga.robot.page.SellerListPage;

/**
 * MusicRobot
 * @author agustina.dagnino
 *
 */
public abstract class MusicRobot implements Robot {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MusicRobot.class.getName());

    private static final String TAXES_MULTIPLIER = "taxes.multiplier";
    private static final String BUSINESS_MARGIN = "business.margin";
    private static final String SHIPPING_PRICE_PER_KILO = "shipping.pricePerKilo";
    private static final String DOLLAR_OFFICIAL = "dolLar.official";
    private static final String DOLLAR_CREDIT_CARD = "dolLar.creditCard";
    private static final String LOCAL_PATH = "local.path";

    
    /**
     * getPublication
     * @param url
     * @param driver
     * @return
     */
    protected List<Publication> getPublications( String url, WebDriver driver ){
        
        List<Publication> list = new ArrayList<Publication>();
        
        try {
            
            // open browser to requested url
            MusicPage musicPage = new MusicPage(driver).go(url);
            musicPage.waitForPopups();
            
            if( musicPage != null )
            {
                List<String> urls = musicPage.getEditionsUrls();
                
                // change javascript call into current url
                List<String> validatedUrls = new ArrayList<String>();
                for (String string : urls) {
                    validatedUrls.add(validateUrl(string,url));
                }
                
                // place current url at front to avoid extra calls
                if(validatedUrls.contains(url)){
                    urls = new ArrayList<String>();
                    urls.add(url);
                    for (String string : validatedUrls) {
                        if(!string.equals(url)){
                            urls.add(string);
                        }
                    }
                } else {
                    urls = validatedUrls;
                }
                
                // get publication for each url
                for (String anUrl : urls) {
                    if(!shouldIgnoreUrl(anUrl)){
                        Publication ret = this.getPublication(anUrl, driver, musicPage, !url.equals(anUrl));
                        list.add(ret);
                    }
                }
        
            }    
            
        } catch (Exception e) {
            
            // take screenshot of error
            SeleniumUtils.captureScreenshot(driver);
            
            // log exception
            LOGGER.error("Error reading URL "+url, e);
            
        } 
       
        return list;
    }
    
    /**
     * validateUrl
     * @param editionUrl
     * @param url
     * @return
     */
    private String validateUrl(String editionUrl, String url) {
        String ret = editionUrl;
        if( editionUrl!=null && editionUrl.startsWith("javascript")){
            ret = url;
        }
        return ret;
    }

    /**
     * shouldIgnoreUrl
     * @param url
     * @return
     */
    protected boolean shouldIgnoreUrl(String url) {
        return StringUtils.isEmpty(url);
    }

    /**
     * getPublication
     * @param url
     * @param driver
     * @return
     */
    protected Publication getPublication( String url, WebDriver driver, MusicPage aMusicPage , boolean shouldReload ){
        
        Publication ret = new Publication();
        ret.setUrl(url);
        
        try {
            
            // open browser to requested url
            MusicPage musicPage = aMusicPage;
            if( shouldReload ){
                musicPage = new MusicPage(driver).go(url);
                musicPage.waitForPopups();
            }
            
            if( musicPage != null )
            {
                // read data form current page
                Music music = getMusicData(musicPage, driver);
                
                // set the publication data
                ret.setProduct(music);
                ret.setTitle(getPublicationTitle(music));
                ret.setPrice(getPublicationPrice(music));
                ret.setDescription(getPublicationDescription(music));
                ret.setImages(music.getImages());
            }    
            
        } catch (Exception e) {
            
            // take screenshot of error
            SeleniumUtils.captureScreenshot(driver);
            
            // log exception
            LOGGER.error("Error reading URL "+url, e);
            
        } 
       
        return ret;
    }

    /**
     * getMusicData
     * @param musicPage
     * @return
     */
    public Music getMusicData(MusicPage musicPage, WebDriver driver) {
        
        Music music = getMusicDetails(musicPage);
        musicPage.openPhotoViewer();
        music.setImages(getImages(musicPage.getImages()));
        
        // if we still did not get an amazon price, look it up
        if( amazonPriceIsNotSet(music) ){
            String sellerListUrl = musicPage.getSellerListUrl();
            if( StringUtils.isEmpty(sellerListUrl) ){
                LOGGER.info("Seller list not available");
            } else {
                SellerListPage sellerListPage = new SellerListPage(driver).go(sellerListUrl); 
                //sellerListPage.filterNew();
                boolean loaded = true;
                do {
                    if(!loaded){
                        sellerListPage = new SellerListPage(driver).go(sellerListUrl);
                    }
                    loaded = false;
                    List<SellerQuote> sellerQuotes = sellerListPage.getSellerQuotes();
                    SellerQuote amazon = findAmazonQuote(sellerQuotes);
                    if( amazon!=null ){
                        music.setPrice(amazon.getPrice());
                        music.setSeller(amazon.getSeller());
                        sellerListUrl = null;
                    } else {
                        sellerListUrl = sellerListPage.nextPageUrl();
                    }
                } while( sellerListUrl!=null );
            }
        }
        
        return music;
    }

    /**
     * findAmazonQuote
     * @param sellerQuotes
     * @return
     */
    private SellerQuote findAmazonQuote(List<SellerQuote> sellerQuotes) {
        SellerQuote ret = null;
        for (SellerQuote sellerQuote : sellerQuotes) {
            if("Amazon.com".equals(sellerQuote.getSeller())){
                ret = sellerQuote;
                break;
            }
        }
        return ret;
    }

    /**
     * amazonPriceIsSet
     * @param music
     * @return
     */
    public boolean amazonPriceIsNotSet(Music music) {
        return StringUtils.isEmpty(music.getPrice()) 
                    || StringUtils.isEmpty(music.getSeller()) 
                    || ! ( "Amazon.com".equals(music.getSeller())
                            || "Vendido y enviado por Amazon.com.".equals(music.getSeller()));
    }

    /**
     * getImages
     * @param images
     * @return
     */
    private List<String> getImages(List<String> images) {
        List<String> ret = new ArrayList<String>();
        if( images!=null && images.size()>0 ){
            int count = 0;
            for (String url : images) {
                ret.add(url);
                if((++count)>=9){
                    break;
                }
            }
            // add 365 image
            ret.add("https://http2.mlstatic.com/4k-ultra-hd-blu-ray-harry-potter-collection-8-films-D_NQ_NP_821117-MLA40710388896_022020-F.webp");
        }
        return ret;
    }

    /**
     * getMusicDetails
     * @return
     */
    protected Music getMusicDetails(MusicPage musicPage){
        Music ret = new Music();
        ret.setFormat(musicPage.getFormat());
        ret.setAlbumFormat(musicPage.getAlbumFormat());
        ret.setHasAdditionalTracks(musicPage.hasAdditionalTracks());
        ret.setReleaseYear(musicPage.getReleaseYear());
        ret.setNumberOfDisks(musicPage.getNumberOfDisks());
        ret.setNumberOfSongs(musicPage.getNumberOfSongs());        
        ret.setPrice(musicPage.getPrice());
        ret.setWeight(musicPage.getWeight());
        ret.setAvailability(musicPage.getAvailability());
        ret.setGenre(musicPage.getGenre());
        ret.setOrigin(musicPage.getOrigin());
        ret.setDimensions(musicPage.getDimensions());
        ret.setSeller(musicPage.getSeller());
        return ret;
    }

    
    /**
     * getPublicationTitle
     * @param music
     * @return
     */
    protected String getPublicationTitle(Music music) {
        String title = music.getAlbumFormat() + " - " + music.getArtist() + " - " +music.getAlbum();
        return title;
    }
    
    /**
     * getPublicationPrice
     * @param music
     * @return
     */
    public String getPublicationPrice(Music music) {
        try {
            Number dolarPriceAmount = music.getDolarPriceAmount();
            Number weightInKilos = music.getWeightInKilos();
            if( ( dolarPriceAmount == null ) || ( weightInKilos == null ) ){
                return "";
            }
            
            double taxesMultiplier = getTaxesMultiplier();
            double creditCardDolarQuotation = getCreditCardDolLarQuotation();
            double officialDolarQuotation = getOfficialDoLlarQuotation();
            double shippingPricePerKilo = getShippingPricePerKilo();
            double margin = getMargin();
            
            double costInPesos = dolarPriceAmount.doubleValue() * taxesMultiplier * creditCardDolarQuotation;
            double shippingCostInPesos = weightInKilos.doubleValue() * officialDolarQuotation * shippingPricePerKilo;
            double priceInPesos =  ( costInPesos  + shippingCostInPesos ) * margin ;
            
            if( priceInPesos >= 2500 ){
                priceInPesos = priceInPesos + 190;
            }
            
            return formatNumberAsString( priceInPesos );
        } catch (Exception e) {
            return "Unable to retrieve publication price";
        }
    }

    /**
     * formatNumberAsString
     * @param d
     * @return
     */
    private String formatNumberAsString(double d) {
        return String.format( "%.2f", d ) ; 
    }

    /**
     * getTaxesMultiplier
     * @return
     */
    private double getTaxesMultiplier() {
        return getPropertyAsDouble(TAXES_MULTIPLIER);
    }
    
    /**
     * getOfficialDoLlarQuotation
     * @return
     */
    private double getOfficialDoLlarQuotation() {
        return getPropertyAsDouble(DOLLAR_OFFICIAL);
    }

    /**
     * getPropertyAsDouble
     * @param property
     * @return double value
     */
    private double getPropertyAsDouble(String property) {
        AppProperties config = AppProperties.getInstance();    
        String quotation = config.getProperty(property);
        return StringUtils.parse(quotation).doubleValue();
    }
    
    /**
     * getPricePerKilo
     * @return
     */
    private double getShippingPricePerKilo() {
        return getPropertyAsDouble(SHIPPING_PRICE_PER_KILO);
    }

    /**
     * getMargin
     * @return
     */
    private double getMargin() {
        return getPropertyAsDouble(BUSINESS_MARGIN);
    }

    /**
     * getCreditCardDolLarQuotation
     * @return
     */
    private double getCreditCardDolLarQuotation() {
        return getPropertyAsDouble(DOLLAR_CREDIT_CARD);
    }

    /**
     * getPublicationDescription
     * @param music
     * @return
     */
    private String getPublicationDescription(Music music) {
        return "365CINE."+ lineBreak()+
               "CARACTERISTICAS: NUEVO / ORIGINAL / IMPORTADO"+ lineBreak()+
               getCharacteristicDescription("DIMENSIONES",music.getDimensions())+
               getCharacteristicDescription("SELLO",music.getLabel())+
               getCharacteristicDescription("CANTIDAD DE DISCOS",music.getNumberOfDisks())+
               getCharacteristicDescription("FORMATO",music.getFormat())+
               "IMPORTAMOS ESTE PRODUCTO CON UNA DEMORA APROXIMADA DE 25 A 30 DIAS."+  lineBreak()+
               "SE RETIRA POR CAPITAL FEDERAL."+ lineBreak()+ 
               "ENVIOS A DOMICILIO A TODO EL PAIS."+ lineBreak()+ 
               "MEDIOS DE PAGO ACEPTADOS: MERCADO PAGO (INCLUYE TARJETAS DE CREDITO Y DEBITO) // RAPIPAGO // PAGO FACIL."+lineBreak()+  
               "SOLO PRODUCTOS 100% ORIGINALES."+ lineBreak()+ 
               "365CINE";
    }

    /**
     * getCharacteristicDescription
     * @param key
     * @param value
     * @return
     */
    private String getCharacteristicDescription(String key, String value) {
        return (!StringUtils.isEmpty(value))? 
                    (key+": "+value+lineBreak()):
                    "";
    }

    /**
     * lineBreak
     * @return
     */
    private String lineBreak() {
        return "\n";
    }
    

    /**
     * validatePropertyIsNotEmpty
     * @param config
     * @param property
     * @return
     */
    protected boolean validatePropertyIsNotEmpty(AppProperties config, String property) {
        boolean ret = true;
        String businessMargin = config.getProperty(property);
        if( businessMargin==null || "".equals(businessMargin.trim()) ){
            LOGGER.error(property+" is empty");
            ret = false;  
        }
        return ret;
    }
    
    /**
     * validConfig
     */
    @Override
    public boolean validConfig() {
        
        boolean ret = true;
        
        AppProperties config = AppProperties.getInstance();
        
        ret &= validatePropertyIsNotEmpty(config,LOCAL_PATH);
        if (ret) {
            String localPath = config.getProperty(LOCAL_PATH);
            File dir = new File(localPath);
            if( !dir.exists() ){
                LOGGER.error("local.path does not exist. Please create it.");
                ret = false;
            }
        }
        
        ret &= validatePropertyIsNotEmpty(config,DOLLAR_CREDIT_CARD);
        
        ret &= validatePropertyIsNotEmpty(config,DOLLAR_OFFICIAL);
        
        ret &= validatePropertyIsNotEmpty(config,SHIPPING_PRICE_PER_KILO);
        
        ret &= validatePropertyIsNotEmpty(config,BUSINESS_MARGIN);
        
        ret &= validatePropertyIsNotEmpty(config,TAXES_MULTIPLIER);
        
        return ret;
    }
    
    /**
     * login
     */
    public void login( WebDriver driver ){
        String url = "https://www.amazon.com/ap/signin?_encoding=UTF8&ignoreAuthState=1&openid.assoc_handle=usflex&openid.claimed_id=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0%2Fidentifier_select&openid.identity=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0%2Fidentifier_select&openid.mode=checkid_setup&openid.ns=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0&openid.ns.pape=http%3A%2F%2Fspecs.openid.net%2Fextensions%2Fpape%2F1.0&openid.pape.max_auth_age=0&openid.return_to=https%3A%2F%2Fwww.amazon.com%2F-%2Fes%2FOctavia-Butler%2Fdp%2F1583226982%2Fref%3Dnav_signin%3Fqid%3D1582944662%26refinements%3Dp_n_feature_browse-bin%253A2656022011%26rnid%3D618072011%26s%3Dbooks%26sr%3D1-217&switch_account=";
        LoginPage page = new LoginPage(driver);
        page.go(url);
        waitForInput();
    }

    /**
     * waitForInput
     */
    private void waitForInput() {
        Scanner in = null;
        try {
            in = new Scanner(System.in);
            String s = in. nextLine();
            System.out. println("You entered string "+s);
        } catch (Exception e){
            LOGGER.error(e.getMessage());
        } finally {
            if( in!=null ){
                in.close();
            }
        }

    }
       
}
