package com.robot.types;

import java.io.File;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.robot.Robot;
import com.robot.RobotProperties;
import com.robot.model.Book;
import com.robot.model.Publication;
import com.robot.page.BookPage;
import com.robot.utils.SeleniumUtils;
import com.robot.utils.StringUtils;

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

    private static final String BUSINESS_MARGIN = "business.margin";
    private static final String SHIPPING_PRICE_PER_KILO = "shipping.pricePerKilo";
    private static final String DOLLAR_OFFICIAL = "dolLar.official";
    private static final String DOLLAR_CREDIT_CARD = "dolLar.creditCard";
    private static final String LOCAL_PATH = "local.path";
    private static final String FORCE_PAPERBACK = "force.paperback";

    
    /**
     * getPublication
     * @param url
     * @param driver
     * @return
     */
    protected Publication getPublication( String url, WebDriver driver ){
        
        Publication ret = new Publication();
        
        try {
            ret.setUrl(url);
            
            // open browser to requested url
            BookPage bookPage = new BookPage(driver).go(url);
                
            if( bookPage != null )
            {
                if( this.getPropertyAsBoolean(FORCE_PAPERBACK, false)) {
                    bookPage.selectPaperbackEdition();
                }
                
                Book book = getBook(bookPage);
                ret.setProduct(book);
                
                ret.setTitle(getPublicationTitle(book));
                ret.setPrice(getPublicationPrice(book));
                ret.setDescription(getPublicationDescription(book));
                
                bookPage.openPhotoViewer();
                ret.setImages(getImages(bookPage.getImages()));
                //ret.setImages(getPublicationImages(book));
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
     * getImages
     * @param images
     * @return
     */
    private List<String> getImages(List<String> images) {
        if( images!=null && images.size()>0 ){
            images.add("https://http2.mlstatic.com/4k-ultra-hd-blu-ray-harry-potter-collection-8-films-D_NQ_NP_821117-MLA40710388896_022020-F.webp");
            //images.add("https://i.postimg.cc/0NsFBwvH/365cine-logo-2.png");
        }
        return images;
    }

    /**
     * getBook
     * @return
     */
    public Book getBook(BookPage bookPage){
        bookPage.waitForPopups();
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
    private String getPublicationTitle(Book book) {
        String title = "Libro "+book.getTitle();
        if( (title.length()+3+book.getAuthor().length()) <= 60 ){
            title = title + " / " + book.getAuthor();
        }
        if(title.length()>60){
            title = title.substring(0, 60);
        }
        return title;
    }

    /**
     * getPublicationPrice
     * @param book
     * @return
     */
    private String getPublicationPrice(Book book) {
        
        Number dolarPriceAmount = book.getDolarPriceAmount();
        Number weightInKilos = book.getWeightInKilos();
        if( ( dolarPriceAmount == null ) || ( weightInKilos == null ) ){
            return "";
        }
        
        double creditCardDolarQuotation = getCreditCardDolLarQuotation();
        double officialDolarQuotation = getOfficialDoLlarQuotation();
        double shippingPricePerKilo = getShippingPricePerKilo();
        double margin = getMargin();
        
        double costInPesos = dolarPriceAmount.doubleValue() * creditCardDolarQuotation;
        double shippingCostInPesos = weightInKilos.doubleValue() * officialDolarQuotation * shippingPricePerKilo;
        double priceInPesos =  ( costInPesos  + shippingCostInPesos ) * margin ;
        
        return formatNumberAsString( priceInPesos );
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
        RobotProperties config = RobotProperties.getInstance();    
        String quotation = config.getProperty(property);
        return StringUtils.parse(quotation).doubleValue();
    }
    
    /**
     * getPropertyAsBoolean
     * @param property
     * @param defaultValue
     * @return
     */
    private boolean getPropertyAsBoolean(String property,boolean defaultValue){
        boolean ret = defaultValue;
        RobotProperties config = RobotProperties.getInstance();    
        String value = config.getProperty(property);
        if( !StringUtils.isEmpty(value) ){
            ret = Boolean.parseBoolean(value);
        }
        return ret;
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
    protected boolean validatePropertyIsNotEmpty(RobotProperties config, String property) {
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
        
        RobotProperties config = RobotProperties.getInstance();
        
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
        
        
        return ret;
    }
       
}
