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
import com.anitsuga.fwk.utils.Browser;
import com.anitsuga.fwk.utils.SeleniumUtils;
import com.anitsuga.fwk.utils.StringUtils;
import com.anitsuga.robot.Robot;
import com.anitsuga.robot.RobotURLProvider;
import com.anitsuga.robot.model.Book;
import com.anitsuga.robot.model.Product;
import com.anitsuga.robot.model.Publication;
import com.anitsuga.robot.model.SellerQuote;
import com.anitsuga.robot.page.ProductPage;
import com.anitsuga.robot.page.SellerListPage;

/**
 * BookRobot
 * @author agustina.dagnino
 *
 */
public abstract class AbstractRobot implements Robot {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRobot.class.getName());

    protected static final String TAXES_MULTIPLIER = "taxes.multiplier";
    protected static final String BUSINESS_MARGIN = "business.margin";
    protected static final String SHIPPING_PRICE_PER_KILO = "shipping.pricePerKilo";
    protected static final String DOLLAR_OFFICIAL = "dolLar.official";
    protected static final String DOLLAR_CREDIT_CARD = "dolLar.creditCard";
    protected static final String LOCAL_PATH = "local.path";

    private RobotURLProvider urlProvider;
    private boolean shouldNavigateURLs;
    
    /**
     * setURLProvider
     */
    @Override
    public void setURLProvider(RobotURLProvider urlProvider) {
        this.urlProvider = urlProvider;
    }
    
    /**
     * scrape
     */
    @Override
    public List<Publication> scrape() {
        
        WebDriver driver = SeleniumUtils.buildDriver(Browser.CHROME);
        urlProvider.setWebDriver(driver);
        login(driver);
        
        List<Publication> ret = new ArrayList<Publication>();
        List<String> productUrls = urlProvider.getURLs();
        
        int total = productUrls.size();
        int count = 0;
        for (String bookUrl : productUrls) {
            LOGGER.info("Reading product ["+(++count)+"/"+total+"] from url "+bookUrl);
            if(shouldNavigateURLs){
                List<Publication> publications = getPublications(bookUrl, driver);
                ret.addAll(publications);
            } else {
                Publication publication = this.getPublication(bookUrl, driver, null, true);
                ret.add(publication);
            }
        }
        
        return ret;
    }
    
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
            ProductPage productPage = goToProductPage(url, driver);
            productPage.waitForPopups();
            
            if( productPage != null )
            {
                List<String> urls = productPage.getEditionsUrls();
                
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
                        Publication ret = this.getPublication(anUrl, driver, productPage, !url.equals(anUrl));
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
     * goToProductPage
     * @param url
     * @param driver
     * @return
     */
    protected abstract ProductPage goToProductPage(String url, WebDriver driver);
    
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
        return StringUtils.isEmpty(url) || urlProvider.shouldIgnoreUrl(url);
    }

    /**
     * getPublication
     * @param url
     * @param driver
     * @return
     */
    protected Publication getPublication( String url, WebDriver driver, ProductPage aBookPage , boolean shouldReload ){
        
        Publication ret = new Publication();
        ret.setUrl(url);
        
        try {
            
            // open browser to requested url
            ProductPage productPage = (ProductPage) aBookPage;
            if( shouldReload ){
                productPage = goToProductPage(url, driver);
                productPage.waitForPopups();
            }
            
            if( productPage != null )
            {
                // read data form current page
                Product product = getProductData(productPage, driver);
                
                // set the publication data
                ret.setProduct(product);
                ret.setTitle(getPublicationTitle(product));
                ret.setPrice(getPublicationPrice(product));
                ret.setDescription(getPublicationDescription(product));
                ret.setImages(product.getImages());
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
     * getPublicationDescription
     * @param product
     * @return
     */
    protected abstract String getPublicationDescription(Product product);

    /**
     * getPublicationTitle
     * @param product
     * @return
     */
    protected abstract String getPublicationTitle(Product product);

    /**
     * getProductData
     * @param productPage
     * @return
     */
    public Product getProductData(ProductPage productPage, WebDriver driver) {
        
        Product book = getProductDetails(productPage);
        productPage.openPhotoViewer();
        book.setImages(getImages(productPage.getImages()));
        
        // if we still did not get an amazon price, look it up
        if( amazonPriceIsNotSet(book) ){
            String sellerListUrl = productPage.getSellerListUrl();
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
                        book.setPrice(amazon.getPrice());
                        book.setSeller(amazon.getSeller());
                        sellerListUrl = null;
                    } else {
                        sellerListUrl = sellerListPage.nextPageUrl();
                    }
                } while( sellerListUrl!=null );
            }
        }
        
        return book;
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
     * @param product
     * @return
     */
    public boolean amazonPriceIsNotSet(Product product) {
        return StringUtils.isEmpty(product.getPrice()) 
                    || StringUtils.isEmpty(product.getSeller()) 
                    || ! ( "Amazon.com".equals(product.getSeller())
                            || "Vendido y enviado por Amazon.com.".equals(product.getSeller()));
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
     * getProductDetails
     * @return
     */
    protected abstract Product getProductDetails(ProductPage page);


    /**
     * getNumberOfOccurrences
     * @param title
     * @param string
     * @return
     */
    protected int getNumberOfOccurrences(String title, String pattern) {
        int count = 0;
        int index=0;
        while(index>=0){
            index = title.indexOf(pattern,index+1);
            if(index>0){
                count++;
            }
        }
        return count;
    }

    /**
     * getLastSeparatorCharacterIndex
     * @param title
     * @return
     */
    protected int getLastSeparatorCharacterIndex(String title) {
        int lastSeparatorIndex = title.length();
        int fromIndex = 0;
        while(fromIndex>=0){
            fromIndex = title.indexOf(" ", fromIndex+1);
            if(fromIndex>=0){
                lastSeparatorIndex = fromIndex;
            }
        }
        return lastSeparatorIndex;
    }

    /**
     * getPublicationPrice
     * @param product
     * @return
     */
    public abstract String getPublicationPrice(Product product) ;

    /**
     * formatNumberAsString
     * @param d
     * @return
     */
    protected String formatNumberAsString(double d) {
        return String.format( "%.2f", d ) ; 
    }

    /**
     * getTaxesMultiplier
     * @return
     */
    protected double getTaxesMultiplier() {
        return getPropertyAsDouble(TAXES_MULTIPLIER);
    }
    
    /**
     * getOfficialDoLlarQuotation
     * @return
     */
    protected double getOfficialDoLlarQuotation() {
        return getPropertyAsDouble(DOLLAR_OFFICIAL);
    }

    /**
     * getPropertyAsDouble
     * @param property
     * @return double value
     */
    protected double getPropertyAsDouble(String property) {
        AppProperties config = AppProperties.getInstance();    
        String quotation = config.getProperty(property);
        return StringUtils.parse(quotation).doubleValue();
    }
    
    /**
     * getPricePerKilo
     * @return
     */
    protected double getShippingPricePerKilo() {
        return getPropertyAsDouble(SHIPPING_PRICE_PER_KILO);
    }

    /**
     * getMargin
     * @return
     */
    protected double getMargin() {
        return getPropertyAsDouble(BUSINESS_MARGIN);
    }

    /**
     * getCreditCardDolLarQuotation
     * @return
     */
    protected double getCreditCardDolLarQuotation() {
        return getPropertyAsDouble(DOLLAR_CREDIT_CARD);
    }

    /**
     * getCharacteristicDescription
     * @param key
     * @param value
     * @return
     */
    protected String getCharacteristicDescription(String key, String value) {
        return (!StringUtils.isEmpty(value))? 
                    (key+": "+value+lineBreak()):
                    "";
    }

    /**
     * lineBreak
     * @return
     */
    protected String lineBreak() {
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

    @Override
    public void shouldNavigateURLs(boolean shouldNavigateURLs) {
        this.shouldNavigateURLs = shouldNavigateURLs;
    }
       
}
