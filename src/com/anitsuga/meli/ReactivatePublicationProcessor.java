package com.anitsuga.meli;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anitsuga.fwk.utils.SeleniumUtils;
import com.anitsuga.meli.model.Operation;
import com.anitsuga.meli.model.Publication;
import com.anitsuga.meli.page.PublicationEditPage;

/**
 * ReactivatePublicationProcessor
 * @author agustina
 *
 */
public class ReactivatePublicationProcessor extends Processor {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ReactivatePublicationProcessor.class.getName());
    
    
    /**
     * main
     * @param args
     */
    public static void main(String[] args) {
        ReactivatePublicationProcessor self = new ReactivatePublicationProcessor();
        self.run();
    }

    /**
     * doProcess
     * @param data
     * @param driver
     */
    protected List<Operation> doProcess(List<Publication> data, WebDriver driver) {
        this.login(driver);
        return this.updateStatuses(driver,data);
    }

    /**
     * updatePrupdateStatusices
     * @param data
     * @return
     */
    private List<Operation> updateStatuses(WebDriver driver, List<Publication> data) {
        int total = data.size();
        int count = 0;
        List<Operation> ret = new ArrayList<Operation>();
        for (Publication publication : data) {
            LOGGER.info("Reactivating publication ["+(++count)+"/"+total+"] - "+publication.getTitle()+" ("+publication.getId()+")");
            String result = reactivatePublication(driver,publication);
            Operation op = new Operation();
            op.setPublication(publication);
            op.setResult(result);
            LOGGER.info("    "+result);  
            ret.add(op);
        }
        return ret;
    }
    
    /**
     * reactivatePublication
     * @param driver
     * @param publication
     * @return
     */
    private String reactivatePublication(WebDriver driver, Publication publication) {
        
        String ret = "Undefined";
        
        try {
            
            if( StringUtils.isEmpty(publication.getId()) ){
                return "Id is empty";
            }
            
            PublicationEditPage publicationPage = goToEditPage(driver, publication);
            
            if( titlesMatch(publication, publicationPage) ){
                if( !quantityIsAsExpected(publicationPage) ){
                    publicationPage.setQuantity(this.getExpectedQuantity());
                    publicationPage.commit();
                    ret = publicationPage.waitForSave();
                }
                if( quantityIsAsExpected(publicationPage) ){
                    if( publicationPage.isPaused() ){
                        publicationPage.reactivate();
                        ret = publicationPage.waitForSave();
                    }
                } else {
                    ret = "Quantity set in publication ("+publicationPage.getQuantityValue()+") is not the expected one. Please correct it manually.";
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
     * quantityIsAsExpected
     * @param publicationPage
     * @return
     */
    private boolean quantityIsAsExpected(PublicationEditPage publicationPage) {
        String quantity = publicationPage.getQuantityValue();
        boolean ret = getExpectedQuantity().equals(quantity);
        return ret;
    }

    /**
     * getExpectedQuantity
     * @return
     */
    private String getExpectedQuantity() {
        return "1";
    }
    
    /**
     * outputFilePrefix
     * @return
     */
    @Override
    protected String outputFilePrefix() {
        return "reactivate-publication-processor";
    }

}
