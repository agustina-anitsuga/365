package com.anitsuga.robot.types;

import org.openqa.selenium.WebDriver;

import com.anitsuga.robot.Robot;
import com.anitsuga.robot.model.Pencil;
import com.anitsuga.robot.model.Music;
import com.anitsuga.robot.model.Product;
import com.anitsuga.robot.page.PencilPage;
import com.anitsuga.robot.page.ProductPage;

/**
 * PencilRobot
 * @author agustina.dagnino
 *
 */
public class PencilRobot extends AbstractRobot implements Robot {

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
        PencilPage pencilPage = (PencilPage) productPage;
        Pencil ret = new Pencil();
        ret.setAmazonID(pencilPage.getAmazonID());
        ret.setPrice(pencilPage.getPrice());
        ret.setWeight(pencilPage.getWeight());
        ret.setAvailability(pencilPage.getAvailability());
        ret.setSeller(pencilPage.getSeller());
        ret.setDistributor(pencilPage.getDistributor());
        ret.setTitle(pencilPage.getTitle());
        ret.setType(pencilPage.getType());
        ret.setBrand(pencilPage.getBrand());
        ret.setModel(pencilPage.getModel());
        ret.setUnitsPerPackage(pencilPage.getUnitsPerPackage());
        ret.setWatercolor(pencilPage.isWatercolor());
        ret.setPastel(pencilPage.isPastel());
        ret.setGrade(pencilPage.getGrade());
        return ret;
    }

    
    /**
     * getPublicationTitle
     * @param music
     * @return
     */
    protected String getPublicationTitle(Product product) {
        Pencil art = (Pencil) product;
        String title = art.getTitle();
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
            Pencil music = (Pencil) product;
            
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
        return new PencilPage(driver).go(url);
    }

}
