package com.anitsuga.robot.types;

import org.openqa.selenium.WebDriver;

import com.anitsuga.robot.Robot;
import com.anitsuga.robot.model.Movie;
import com.anitsuga.robot.model.Product;
import com.anitsuga.robot.page.MoviePage;
import com.anitsuga.robot.page.ProductPage;

/**
 * MovieRobot
 * @author agustina.dagnino
 *
 */
public class MovieRobot extends AbstractRobot implements Robot {

    /**
     * amazonPriceIsNotSet
     */
    public boolean amazonPriceIsNotSet(Product product) {
        boolean amazonPriceIsNotSet = super.amazonPriceIsNotSet(product);
        boolean amazonIsDistributor = "Amazon".equals(product.getDistributor());
        return amazonPriceIsNotSet?  !amazonIsDistributor : amazonPriceIsNotSet;
    }
    
    /**
     * getProductDetails
     * @return
     */
    protected Product getProductDetails(ProductPage productPage){
        MoviePage moviePage = (MoviePage) productPage;
        Movie ret = new Movie();
        
        ret.setAmazonID(moviePage.getAmazonID());
        ret.setFormat(moviePage.getFormat());
        ret.setNumberOfDisks(moviePage.getNumberOfDisks());
        ret.setPrice(moviePage.getPrice());
        ret.setWeight(moviePage.getWeight());
        ret.setAvailability(moviePage.getAvailability());
        ret.setGenre(moviePage.getGenre());
        ret.setSeller(moviePage.getSeller());
        ret.setDistributor(moviePage.getDistributor());
        ret.setTitle(moviePage.getTitle());
        ret.setDirector(moviePage.getDirector());
        ret.setVideoResolution(moviePage.getVideoResolution());
        ret.setAudio(moviePage.getAudio());
        ret.setStudio(moviePage.getStudio());
        ret.setDimensions(moviePage.getDimensions());
        
        ret.setRunningTime(moviePage.getRunningTime());
        ret.setReleaseDate(moviePage.getReleaseDate());
        ret.setCast(moviePage.getCast());
        ret.setSubtitles(moviePage.getSubtitles());
        ret.setProducers(moviePage.getProducers());
        ret.setWriters(moviePage.getWriters());
        
        return ret;
    }

    
    /**
     * getPublicationTitle
     * @param product
     * @return
     */
    protected String getPublicationTitle(Product product) {
        Movie movie = (Movie) product;
        String title = movie.getFormat() + " " + movie.getTitle();
        return title;
    }
    
    /**
     * getPublicationPrice
     * @param music
     * @return  
     * 
     * 
     * costo = ( ( (precio + tax) * dolar tarjeta ) * 1.4 ) * 1.6
     * 
     */
    public String getPublicationPrice(Product product) {
        try {
            Movie music = (Movie) product;
            
            Number dolarPriceAmount = music.getDolarPriceAmount();
            
            double taxesMultiplier = getTaxesMultiplier();
            double creditCardDolarQuotation = getCreditCardDolLarQuotation();
            double margin = getMargin();
            
            double costInPesos = dolarPriceAmount.doubleValue() * taxesMultiplier * creditCardDolarQuotation * 1.4 ;
            
            double priceInPesos = costInPesos * margin ;
            
            if( priceInPesos >= 2500 ){
                priceInPesos = priceInPesos + 250;
            }
            
            return formatNumberAsString( priceInPesos );
        } catch (Exception e) {
            return "Unable to retrieve publication price";
        }
    }

    /**
     * getPublicationDescription
     * @param music
     * @return
     */
    protected String getPublicationDescription(Product product) {
        Movie movie = (Movie) product;
        return "365CINE."+ lineBreak()+
               "CARACTERISTICAS: NUEVO / ORIGINAL / IMPORTADO"+ lineBreak()+
               lineBreak() +               
               getCharacteristicDescription("DIMENSIONES",movie.getDimensions())+
               getCharacteristicDescription("CANTIDAD DE DISCOS",movie.getNumberOfDisks())+
               getCharacteristicDescription("FORMATO",movie.getFormat())+
               getCharacteristicDescription("IDIOMA",movie.getAudio())+
               getCharacteristicDescription("SUBTITULOS",movie.getSubtitles())+
               getCharacteristicDescription("DIRECTOR",movie.getDirector())+
               getCharacteristicDescription("ACTORES",movie.getCast())+
               getCharacteristicDescription("PRODUCTORES",movie.getProducers())+
               getCharacteristicDescription("ESCRITORES",movie.getWriters())+
               getCharacteristicDescription("DURACION",movie.getRunningTime())+
               getCharacteristicDescription("ESTUDIO",movie.getStudio())+
               getCharacteristicDescription("LANZAMIENTO",movie.getReleaseDate())+
               lineBreak() +
               (this.isDropShippingEnabled()? "IMPORTAMOS ESTE PRODUCTO CON UNA DEMORA APROXIMADA DE 25 A 30 DIAS."+  lineBreak() : "" )+
               "SE RETIRA POR CAPITAL FEDERAL."+ lineBreak()+ 
               "ENVIOS A DOMICILIO A TODO EL PAIS."+ lineBreak()+ 
               "MEDIOS DE PAGO ACEPTADOS: MERCADO PAGO (INCLUYE TARJETAS DE CREDITO Y DEBITO) // RAPIPAGO // PAGO FACIL."+lineBreak()+  
               "SOLO PRODUCTOS 100% ORIGINALES."+ lineBreak()+ 
               "365CINE";
    }


    /**
     * goToProductPage
     * @param url
     * @param driver
     * @return
     */
    protected ProductPage goToProductPage(String url, WebDriver driver) {
        return new MoviePage(driver).go(url);
    }

}
