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
 * AvailabilityUpdater
 * @author agustina
 *
 */
public class AvailabilityUpdater extends Processor {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AvailabilityUpdater.class.getName());
    
    
    /**
     * main
     * @param args
     */
    public static void main(String[] args) {
        AvailabilityUpdater self = new AvailabilityUpdater();
        self.run();
    }

    /**
     * doProcess
     * @param data
     * @param driver
     */
    protected List<Operation> doProcess(List<Publication> data, WebDriver driver) {
        this.login(driver);
        return this.updateAvailability(driver,data);
    }

    /**
     * updatePrices
     * @param data
     * @return
     */
    private List<Operation> updateAvailability(WebDriver driver, List<Publication> data) {
        int total = data.size();
        int count = 0;
        List<Operation> ret = new ArrayList<Operation>();
        for (Publication publication : data) {
            LOGGER.info("Updating availability ["+count+"/"+total+"] - "+publication.getTitle()+" ("+publication.getId()+")");
            String result = update(driver,publication);
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
    private String update(WebDriver driver, Publication publication) {
        
        String ret = "Undefined";
        
        try {
            
            if( StringUtils.isEmpty(publication.getId()) ){
                return "Id is empty";
            }
            
            PublicationEditPage publicationPage = goToEditPage(driver, publication);
            
            if( titlesMatch(publication, publicationPage) ){
                publicationPage.openAvailabilitySection();
                if( !availabilityIsAsExpected(publicationPage) ){
                    publicationPage.setAvailability(getExpectedAvailability());
                    publicationPage.commitAvailability();
                    ret = publicationPage.waitForSave();
                    if( !availabilityIsAsExpected(publicationPage) ){
                        ret = "Availability set in publication ("+publicationPage.getAvailability()+") is not the expected one. Please correct it manually.";
                    } 
                } else {
                    ret = "Availability did not need to be updated.";
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
     * getExpectedAvailability
     * @return
     */
    private String getExpectedAvailability() {
        return "30";
    }

    /**
     * availabilityIsAsExpected
     * @param publicationPage
     * @return
     */
    private boolean availabilityIsAsExpected(PublicationEditPage publicationPage) {
        String availability = publicationPage.getAvailability();
        boolean ret = getExpectedAvailability().equals(availability);
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
     * outputFilePrefix
     * @return
     */
    @Override
    protected String outputFilePrefix() {
        return "availability-updater";
    }

}
