package com.anitsuga.robot.types;

import org.openqa.selenium.WebDriver;

import com.anitsuga.robot.Robot;
import com.anitsuga.robot.model.Book;
import com.anitsuga.robot.model.Product;
import com.anitsuga.robot.page.BookPage;
import com.anitsuga.robot.page.ProductPage;

/**
 * BookRobot
 * @author agustina.dagnino
 *
 */
public class BookRobot extends AbstractRobot implements Robot {

    /**
     * goToProductPage
     * @param url
     * @param driver
     * @return
     */
    protected ProductPage goToProductPage(String url, WebDriver driver) {
        return new BookPage(driver).go(url);
    }

    /**
     * getProductDetails
     * @return
     */
    protected Product getProductDetails(ProductPage productPage){
        Book ret = new Book();
        BookPage bookPage = (BookPage) productPage;
        ret.setCover(bookPage.getCover());
        ret.setCoverFullData(bookPage.getCoverFullData());
        ret.setEditorial(bookPage.getEditorial());
        ret.setFormat(bookPage.getFormat());
        ret.setGenericIdentifier(bookPage.getISBN());
        ret.setPrice(bookPage.getPrice());
        ret.setTitle(bookPage.getTitle());
        ret.setAuthor(bookPage.getAuthor());
        ret.setWeight(bookPage.getWeight());
        ret.setAvailability(bookPage.getAvailability());
        ret.setLanguage(bookPage.getLanguage());
        ret.setGenre(bookPage.getType());
        ret.setDimensions(bookPage.getDimensions());
        ret.setSeller(bookPage.getSeller());
        return ret;
    }

    
    /**
     * getPublicationTitle
     * @param book
     * @return
     */
    protected String getPublicationTitle(Product product) {
        
        Book book = (Book) product;
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
     * getPublicationPrice
     * @param product
     * @return
     */
    public String getPublicationPrice(Product product) {
        try {
            Book book = (Book) product;
            
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
            
            if( priceInPesos >= 2500 ){
                priceInPesos = priceInPesos + 190;
            }
            
            return formatNumberAsString( priceInPesos );
        } catch (Exception e) {
            return "Unable to retrieve publication price";
        }
    }

    /**
     * getPublicationDescription
     * @param book
     * @return
     */
    protected String getPublicationDescription(Product product) {
        Book book = (Book) product;
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


       
}
