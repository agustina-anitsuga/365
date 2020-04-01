package com.anitsuga.meli;

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
import com.anitsuga.fwk.utils.FileUtils;
import com.anitsuga.fwk.utils.SeleniumUtils;
import com.anitsuga.meli.model.Operation;
import com.anitsuga.meli.model.Publication;
import com.anitsuga.meli.page.PublicationPage;
import com.anitsuga.meli.reader.PriceUpdaterInputReader;
import com.anitsuga.meli.writer.PriceUpdaterResultExcelWriter;

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
             if( data.size() > 0 ){
                 WebDriver driver = SeleniumUtils.buildDriver(Browser.CHROME);
                 this.login(driver);
                 List<Operation> result = this.updatePrices(driver,data);
                 this.writeProcessOutput(result);
             }
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
        String priceToUpdate = "$" + publication.getPriceAsString();
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
        String filename = AppProperties.getInstance().getProperty(INPUT_FILE);
        PriceUpdaterInputReader reader = new PriceUpdaterInputReader(); 
        return reader.read(filename);
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
