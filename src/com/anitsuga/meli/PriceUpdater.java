package com.anitsuga.meli;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anitsuga.meli.model.Operation;
import com.anitsuga.meli.model.Publication;
import com.anitsuga.meli.page.PublicationPage;
import com.anitsuga.meli.writer.PriceUpdaterResultExcelWriter;
import com.anitsuga.page.LoginPage;
import com.anitsuga.utils.AppProperties;
import com.anitsuga.utils.Browser;
import com.anitsuga.utils.FileUtils;
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

    private static final String LOCAL_PATH = "local.path";
    private static final String INPUT_FILE = "input.file";
    
    
    
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
        
         if ( this.validConfig() ) {
             
             List<Publication> data = this.readDataToUpdate();
             
             WebDriver driver = SeleniumUtils.buildDriver(Browser.CHROME);
             
             this.login(driver);
             
             List<Operation> result = this.updatePrices(driver,data);
            
             this.writeProcessOutput(result);
         }
    }

    /**
     * writeProcessOutput
     * @param result
     */
    private void writeProcessOutput(List<Operation> result) {
        String localPath = FileUtils.getLocalPath();
        String filename = localPath + "price-updater";
        PriceUpdaterResultExcelWriter writer = new PriceUpdaterResultExcelWriter(); 
        writer.write(filename, result);
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
            
            PublicationPage publicationPage = goToEditPage(driver, publication);
            
            if( titlesMatch(publication, publicationPage) ){
                if( !pricesMatch(publication, publicationPage) ) {
                    publicationPage.setPrice(publication.getPrice());
                    if( pricesMatch(publication, publicationPage) ){
                        publicationPage.commit();
                        publicationPage.waitForSave();
                        ret = "Price successfully updated.";
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
                System.out.println("");
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
    private boolean titlesMatch(Publication publication, PublicationPage publicationPage) {
        return publication.getTitle().equals(publicationPage.getTitle());
    }

    /**
     * pricesMatch
     * @param publication
     * @param publicationPage
     * @return
     */
    private boolean pricesMatch(Publication publication, PublicationPage publicationPage) {
        String originalPrice = publicationPage.getPriceValue();
        String priceToUpdate = "$" + publication.getPrice().toString();
        return priceToUpdate.equals(originalPrice);
    }

    /**
     * goToEditPage
     * @param driver
     * @param publication
     * @return
     */
    private PublicationPage goToEditPage(WebDriver driver, Publication publication) {
        String editUrl = "https://www.mercadolibre.com.ar/publicaciones/"+publication.getId()+"/modificar";
        PublicationPage publicationPage = new PublicationPage(driver).go(editUrl);
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
     * readDataToUpdate
     * @return
     */
    private List<Publication> readDataToUpdate() {
        List<Publication> data = new ArrayList<Publication>();
        
        Publication publication = new Publication();
        publication.setId("MLA845929856");
        publication.setPrice(82);
        publication.setTitle("Libro - Prueba - No Comprar");
        data.add(publication);
        
        publication = new Publication();
        publication.setId("MLA845929856");
        publication.setPrice(23);
        publication.setTitle("Libro - Prueba - No Comprar");
        data.add(publication);

        publication = new Publication();
        publication.setId("MLA845929856");
        publication.setPrice(82);
        publication.setTitle("Libro - Prueba - No Comprar");
        data.add(publication);
        
        publication = new Publication();
        publication.setId("MLA845929856");
        publication.setPrice(23);
        publication.setTitle("Libro - Prueba - No Comprar");
        data.add(publication);
        
        return data;
    }

    /**
     * validConfig
     */
    public boolean validConfig() {
        
        boolean ret = true;
        
        AppProperties config = AppProperties.getInstance();
        
        ret &= validateFileExists(config,LOCAL_PATH);
        ret &= validateFileExists(config,INPUT_FILE);
        
        return ret;
    }
    
    /**
     * validateFileExists
     * @param config
     * @param property
     * @return
     */
    protected boolean validateFileExists(AppProperties config, String property) {
        boolean ret = validatePropertyIsNotEmpty(config,property);
        if(ret) {
            String localPath = config.getProperty(property);
            File dir = new File(localPath);
            if( !dir.exists() ){
                LOGGER.error(property+" does not exist. Please create it.");
                ret = false;
            }
        }
        return ret;
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
    
}
