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
import com.anitsuga.robot.model.Book;
import com.anitsuga.robot.model.Publication;
import com.anitsuga.robot.model.SellerQuote;
import com.anitsuga.robot.page.BookPage;
import com.anitsuga.robot.page.SellerListPage;

/**
 * BookRobot
 * @author agustina.dagnino
 *
 */
public abstract class BookRobot implements Robot {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BookScraperRobot.class.getName());

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
            BookPage bookPage = new BookPage(driver).go(url);
            bookPage.waitForPopups();
            
            if( bookPage != null )
            {
                List<String> urls = bookPage.getEditionsUrls();
                
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
                        Publication ret = this.getPublication(anUrl, driver, bookPage, !url.equals(anUrl));
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
    protected Publication getPublication( String url, WebDriver driver, BookPage aBookPage , boolean shouldReload ){
        
        Publication ret = new Publication();
        ret.setUrl(url);
        
        try {
            
            // open browser to requested url
            BookPage bookPage = aBookPage;
            if( shouldReload ){
                bookPage = new BookPage(driver).go(url);
                bookPage.waitForPopups();
            }
            
            if( bookPage != null )
            {
                // read data form current page
                Book book = getBookData(bookPage, driver);
                
                // set the publication data
                ret.setProduct(book);
                ret.setTitle(getPublicationTitle(book));
                ret.setPrice(getPublicationPrice(book));
                ret.setDescription(getPublicationDescription(book));
                ret.setImages(book.getImages());
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
     * getBookData
     * @param bookPage
     * @return
     */
    public Book getBookData(BookPage bookPage, WebDriver driver) {
        
        Book book = getBookDetails(bookPage);
        bookPage.openPhotoViewer();
        book.setImages(getImages(bookPage.getImages()));
        
        // if we still did not get an amazon price, look it up
        if( amazonPriceIsNotSet(book) ){
            String sellerListUrl = bookPage.getSellerListUrl();
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
     * @param book
     * @return
     */
    private boolean amazonPriceIsNotSet(Book book) {
        return StringUtils.isEmpty(book.getPrice()) 
                    || StringUtils.isEmpty(book.getSeller()) 
                    || !"Vendido y enviado por Amazon.com.".equals(book.getSeller());
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
     * getBook
     * @return
     */
    protected Book getBookDetails(BookPage bookPage){
        Book ret = new Book();
        ret.setCover(bookPage.getCover());
        ret.setCoverFullData(bookPage.getCoverFullData());
        ret.setEditorial(bookPage.getEditorial());
        ret.setFormat(bookPage.getFormat());
        ret.setIsbn(bookPage.getISBN());
        ret.setPrice(bookPage.getPrice());
        ret.setTitle(bookPage.getTitle());
        ret.setAuthor(bookPage.getAuthor());
        ret.setWeight(bookPage.getWeight());
        ret.setAvailability(bookPage.getAvailability());
        ret.setLanguage(bookPage.getLanguage());
        ret.setType(bookPage.getType());
        ret.setDimensions(bookPage.getDimensions());
        ret.setSeller(bookPage.getSeller());
        return ret;
    }

    
    /**
     * getPublicationTitle
     * @param book
     * @return
     */
    protected String getPublicationTitle(Book book) {
        String title = "Libro "+book.getTitle();
        // remove "Spanish Edition"
        title = title.replaceAll(" \\(Spanish Edition\\)", "");
        title = title.replaceAll(" Spanish Edition", "");
        title = title.replaceAll(" Spanish Version", "");
        title = title.replaceAll(" Spanish Ed.", "");
        title = title.replaceAll(" Spanish Ed", "");
        title = title.replaceAll(" / Spanish", "");
        title = title.trim();
        // remove unexpected ending characters
        if( title.endsWith(":") ){
            title = title.substring(0,title.length()-1);
        }
        // add author if there is room
        if( (title.length()+3+book.getAuthor().length()) <= 60 ){
            title = title + " / " + book.getAuthor();
        }
        // cut the title to 60 chars
        if(title.length()>60){
            title = title.substring(0, 60);
            int lastSeparatorCharacterIndex = getLastSeparatorCharacterIndex(title);
            title = title.substring(0,lastSeparatorCharacterIndex);
            if( title.length() > 56){
                lastSeparatorCharacterIndex = getLastSeparatorCharacterIndex(title);
                title = title.substring(0,lastSeparatorCharacterIndex);
            }
            title = title.concat(" ...");
        }
        // check for unexpected endings after cutting to 60 chars
        if( title.endsWith(":") ){
            title = title.substring(0,title.length()-1);
        }
        String[] endings = new String[]{"(The ...", "(A ..."};
        for (String ending : endings) {     
            if( title.endsWith(ending) ){
                int upto = title.indexOf(ending);
                if(upto>0){
                    title = title.substring(0,upto);
                    title = title.concat("...");
                }
            }
        }
        // check for uneven parenthesis
        int openingParenthesis = getNumberOfOccurrences(title,"(");
        if( openingParenthesis==1 ){
            int closingParenthesis = getNumberOfOccurrences(title,")");
            if( closingParenthesis==0 ){
                title = title.replaceAll("\\(", "/");
            }
        }
        return title;
    }

    /**
     * getNumberOfOccurrences
     * @param title
     * @param string
     * @return
     */
    private int getNumberOfOccurrences(String title, String pattern) {
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
    private int getLastSeparatorCharacterIndex(String title) {
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
     * @param book
     * @return
     */
    public String getPublicationPrice(Book book) {
        try {
            Number dolarPriceAmount = book.getDolarPriceAmount();
            Number weightInKilos = book.getWeightInKilos();
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
     * @param book
     * @return
     */
    private String getPublicationDescription(Book book) {
        return "365CINE."+ lineBreak()+
               "CARACTERISTICAS: NUEVO / ORIGINAL / IMPORTADO"+ lineBreak()+
               getCharacteristicDescription("IDIOMA",book.getLanguage())+
               getCharacteristicDescription("DIMENSIONES",book.getDimensions())+
               getCharacteristicDescription("EDITOR",book.getEditorial())+
               getCharacteristicDescription("FORMATO",book.getCoverFullData())+
               "IMPORTAMOS ESTE PRODUCTO CON UNA DEMORA APROXIMADA DE 20 A 25 DIAS."+  lineBreak()+
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
