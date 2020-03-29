package com.anitsuga.meli;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anitsuga.meli.model.Operation;
import com.anitsuga.meli.model.Publication;
import com.anitsuga.meli.page.PublicationPage;
import com.anitsuga.page.LoginPage;
import com.anitsuga.utils.Browser;
import com.anitsuga.utils.SeleniumUtils;

/**
 * PriceUpdater
 * @author agustina
 *
 */
public class PriceUpdater {

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
     * run
     */
    private void run() {
        
         this.validateConfigurationIsValid();
         
         List<Publication> data = this.readDataToUpdate();
         
         WebDriver driver = SeleniumUtils.buildDriver(Browser.CHROME);
         
         this.login(driver);
         
         List<Operation> result = this.updatePrices(driver,data);
        
         this.writeProcessOutput(result);
    }

    /**
     * writeProcessOutput
     * @param result
     */
    private void writeProcessOutput(List<Operation> result) {
        // TODO Auto-generated method stub
        
    }

    /**
     * updatePrices
     * @param data
     * @return
     */
    private List<Operation> updatePrices(WebDriver driver, List<Publication> data) {
        List<Operation> ret = new ArrayList<Operation>();
        for (Publication publication : data) {
            String result = updatePrice(driver,publication);
            Operation op = new Operation();
            op.setPublication(publication);
            op.setError(result);
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
        
        try {
            
            String editUrl = "https://www.mercadolibre.com.ar/publicaciones/"+publication.getId()+"/modificar";
            PublicationPage publicationPage = new PublicationPage(driver).go(editUrl);
            publicationPage.waitForLoad();
            publicationPage.setPrice(publication.getPrice());
            publicationPage.commit();
            publicationPage.waitForLoad();
            
        } catch (Exception e) {
            
            // take screenshot of error
            SeleniumUtils.captureScreenshot(driver);
            
            // log exception
            LOGGER.error("Error", e);
            
        } 
       
        return "";
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
     * readDataToUpdate
     * @return
     */
    private List<Publication> readDataToUpdate() {
        List<Publication> data = new ArrayList<Publication>();
        Publication publication = new Publication();
        publication.setId("MLA845929856");
        publication.setPrice(82);
        publication.setTitle("Prueba");
        data.add(publication);
        return data;
    }

    /**
     * validateConfigurationIsValid
     */
    private void validateConfigurationIsValid() {
        // TODO Auto-generated method stub
        
    }
    
    
}
