package com.anitsuga.robot.types;

import org.openqa.selenium.WebDriver;

import com.anitsuga.robot.Robot;
import com.anitsuga.robot.model.Music;
import com.anitsuga.robot.model.Product;
import com.anitsuga.robot.page.MusicPage;
import com.anitsuga.robot.page.ProductPage;

/**
 * MusicRobot
 * @author agustina.dagnino
 *
 */
public class MusicRobot extends AbstractRobot implements Robot {


    /**
     * getProductDetails
     * @return
     */
    protected Product getProductDetails(ProductPage productPage){
        MusicPage musicPage = (MusicPage) productPage;
        Music ret = new Music();
        ret.setAmazonID(musicPage.getAmazonID());
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
        ret.setArtist(musicPage.getArtist());
        ret.setAlbum(musicPage.getAlbum());
        return ret;
    }

    
    /**
     * getPublicationTitle
     * @param music
     * @return
     */
    protected String getPublicationTitle(Product product) {
        Music music = (Music) product;
        String title = music.getAlbumFormat() + " " + music.getArtist() + " " +music.getAlbum();
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
            Music music = (Music) product;
            
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
        Music music = (Music) product;
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
     * goToProductPage
     * @param url
     * @param driver
     * @return
     */
    protected ProductPage goToProductPage(String url, WebDriver driver) {
        return new MusicPage(driver).go(url);
    }

}
