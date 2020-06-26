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
 * InactivatePublicationProcessor
 * @author agustina
 *
 */
public class InactivatePublicationProcessor extends Processor {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(InactivatePublicationProcessor.class.getName());
    
    
    /**
     * main
     * @param args
     */
    public static void main(String[] args) {
        InactivatePublicationProcessor self = new InactivatePublicationProcessor();
        self.run();
    }

    /**
     * doProcess
     * @param data
     * @param driver
     */
    protected List<Operation> doProcess(List<Publication> data, WebDriver driver) {
        this.login(driver);
        return this.inactivatePublications(driver,data);
    }

    /**
     * inactivatePublications
     * @param data
     * @return
     */
    private List<Operation> inactivatePublications(WebDriver driver, List<Publication> data) {
        int total = data.size();
        int count = 0;
        List<Operation> ret = new ArrayList<Operation>();
        for (Publication publication : data) {
            LOGGER.info("Inactivating publication ["+(++count)+"/"+total+"] - "+publication.getTitle()+" ("+publication.getId()+")");
            String result = inactivatePublication(driver,publication);
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
    private String inactivatePublication(WebDriver driver, Publication publication) {
        
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
                    if( !quantityIsAsExpected(publicationPage) ){
                        ret = "Quantity set in publication ("+publicationPage.getQuantityValue()+") is not the expected one. Please correct it manually.";
                    }
                } else {
                    ret = "Quantity did not need to be updated.";                    
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
        return "0";
    }

    /**
     * outputFilePrefix
     * @return
     */
    @Override
    protected String outputFilePrefix() {
        return "inactivate-publication-processor";
    }

}
