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
 * StatusUpdater
 * @author agustina
 *
 */
public class StatusUpdater extends Processor {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(StatusUpdater.class.getName());
    
    
    /**
     * main
     * @param args
     */
    public static void main(String[] args) {
        StatusUpdater self = new StatusUpdater();
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
            LOGGER.info("Updating status ["+(++count)+"/"+total+"] - "+publication.getTitle()+" ("+publication.getId()+")");
            String result = updateStatus(driver,publication);
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
    private String updateStatus(WebDriver driver, Publication publication) {
        
        String ret = "Undefined";
        
        try {
            
            if( StringUtils.isEmpty(publication.getId()) ){
                return "Id is empty";
            }
            
            PublicationEditPage publicationPage = goToEditPage(driver, publication);
            
            if( titlesMatch(publication, publicationPage) ){
                publicationPage.reactivate();
                ret = publicationPage.waitForSave();
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
     * outputFilePrefix
     * @return
     */
    @Override
    protected String outputFilePrefix() {
        return "price-updater";
    }

}
