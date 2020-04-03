package com.anitsuga.meli;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anitsuga.fwk.page.LoginPage;
import com.anitsuga.fwk.utils.SeleniumUtils;
import com.anitsuga.meli.model.Operation;
import com.anitsuga.meli.model.Publication;
import com.anitsuga.meli.page.PublicationEditPage;

/**
 * PriceUpdater
 * @author agustina
 *
 */
public class PriceUpdater extends Processor {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PriceUpdater.class.getName());
    
    
    /**
     * main
     * @param args
     */
    public static void main(String[] args) {
        PriceUpdater self = new PriceUpdater();
        self.run();
    }

    /**
     * doProcess
     * @param data
     * @param driver
     */
    protected List<Operation> doProcess(List<Publication> data, WebDriver driver) {
        this.login(driver);
        return this.updatePrices(driver,data);
    }

    /**
     * updatePrices
     * @param data
     * @return
     */
    private List<Operation> updatePrices(WebDriver driver, List<Publication> data) {
        int total = data.size();
        int count = 0;
        List<Operation> ret = new ArrayList<Operation>();
        for (Publication publication : data) {
            LOGGER.info("Updating price ["+count+"/"+total+"] - "+publication.getTitle()+" ("+publication.getId()+")");
            String result = updatePrice(driver,publication);
            Operation op = new Operation();
            op.setPublication(publication);
            op.setResult(result);
            LOGGER.info("    "+result);  
            ret.add(op);
        }
        return ret;
    }
    
    /**
     * 
     * @param driver
     * @param publication
     * @return
     */
    private String updatePrice(WebDriver driver, Publication publication) {
        
        String ret = "Undefined";
        
        try {
            
            if( StringUtils.isEmpty(publication.getId()) ){
                return "Id is empty";
            }
            
            PublicationEditPage publicationPage = goToEditPage(driver, publication);
            
            if( titlesMatch(publication, publicationPage) ){
                if( !pricesMatch(publication, publicationPage) ) {
                    publicationPage.setPrice(publication.getPriceAsString());
                    if( pricesMatch(publication, publicationPage) ){
                        publicationPage.commit();
                        ret = publicationPage.waitForSave();
                        if( !pricesMatch(publication, publicationPage) ){
                            ret = "Price set in publication ("+publicationPage.getPriceValue()+") is not the expected one. Please correct price manually.";
                        }
                    } else {
                        ret = "Price could not be set";
                    }
                } else {
                    ret = "Price did not need to be updated.";
                }
            } else {
                ret = "Publication titles do not match.";
            }
            
        } catch (Exception e) {
            
            // take screenshot of error
            SeleniumUtils.captureScreenshot(driver);
            
            // log exception
            LOGGER.error("Error", e);
            ret = e.getMessage();
        } 
       
        return ret;
    }

    /**
     * titlesMatch
     * @param publication
     * @param publicationPage
     * @return
     */
    private boolean titlesMatch(Publication publication, PublicationEditPage publicationPage) {
        return publication.getTitle().equals(publicationPage.getTitle());
    }

    /**
     * pricesMatch
     * @param publication
     * @param publicationPage
     * @return
     */
    private boolean pricesMatch(Publication publication, PublicationEditPage publicationPage) {
        String originalPrice = publicationPage.getPriceValue();
        String priceToUpdate = "$" + publication.getPriceAsString();
        return priceToUpdate.equals(originalPrice);
    }

    /**
     * goToEditPage
     * @param driver
     * @param publication
     * @return
     */
    private PublicationEditPage goToEditPage(WebDriver driver, Publication publication) {
        String editUrl = "https://www.mercadolibre.com.ar/publicaciones/"+publication.getId()+"/modificar";
        PublicationEditPage publicationPage = new PublicationEditPage(driver).go(editUrl);
        publicationPage.waitForLoad();
        return publicationPage;
    }

    /**
     * login
     */
    public void login( WebDriver driver ){
        String url = "https://www.mercadolibre.com.ar/";
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

    /**
     * outputFilePrefix
     * @return
     */
    @Override
    protected String outputFilePrefix() {
        return "price-updater";
    }

}
