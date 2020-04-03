package com.anitsuga.meli;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anitsuga.meli.model.Operation;
import com.anitsuga.meli.model.Publication;
import com.anitsuga.meli.page.PublicationPage;
import com.anitsuga.meli.page.StorePage;

/**
 * IdRetriever
 * @author agustina
 *
 */
public class IdRetriever extends Processor {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(IdRetriever.class.getName());

    
    /**
     * main
     * @param args
     */
    public static void main(String[] args) {
        IdRetriever self = new IdRetriever();
        self.run();
    }

    /**
     * writeProcessOutput
     */
    @Override
    protected void writeProcessOutput(List<Operation> result) {
        // TODO Auto-generated method stub
        
    }

    /**
     * doProcess
     */
    @Override
    protected List<Operation> doProcess(List<Publication> data, WebDriver driver) {
        int total = data.size();
        int count = 0;
        List<Operation> ret = new ArrayList<Operation>();
        for (Publication publication : data) {
            LOGGER.info("Retrieving id ["+count+"/"+total+"] - "+publication.getTitle()+" ("+publication.getIsbn()+")");
            String result = getId(driver,publication);
            Operation op = new Operation();
            op.setPublication(publication);
            op.setResult(result);
            LOGGER.info("    "+result);  
            ret.add(op);
        }
        return ret;
    }

    /**
     * getId
     * @param driver
     * @param publication
     * @return
     */
    private String getId(WebDriver driver, Publication publication) {
        
        if( StringUtils.isEmpty(publication.getTitle()) ){
            return "Null title";
        }
        
        StorePage store = new StorePage(driver).go("https://eshops.mercadolibre.com.ar/marcelofioren77");
        store.setSearch(publication.getTitle());
        
        StorePage searchResult = store.doSearch();
        List<String> urls = searchResult.getSearchResultUrls();
        
        if(urls.size()==0){
            return "No results found";
        }
         
        if( StringUtils.isEmpty(publication.getIsbn()) ){
            return "Null ISBN. Cannot determine id";
        }
        
        List<String> possibleMatches = new ArrayList<String>();
        for (String url : urls) {
            PublicationPage page = new PublicationPage(driver).go(url);
            if( titlesMatch(publication,page) && isbnsMatch(publication,page) ){
                String id = getIdFromUrl(url);
                possibleMatches.add(id);
            }
        }
        
        if( possibleMatches.size() == 0) {
            return "No matches found";
        }
        
        if( possibleMatches.size() == 0) {
            return possibleMatches.get(0);
        }
        
        String ids = "";
        for (String string : possibleMatches) {
            ids = ids + string + "-" ;
        }
        
        return "Too may matches found. Please Review ("+ids+")";
    }

    /**
     * isbnsMatch
     * @param publication
     * @param page
     * @return
     */
    private boolean isbnsMatch(Publication publication, PublicationPage page) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * titlesMatch
     * @param publication
     * @param page
     * @return
     */
    private boolean titlesMatch(Publication publication, PublicationPage page) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * getIdFromUrl
     * @param string
     * @return
     */
    private String getIdFromUrl(String string) {
        // TODO Auto-generated method stub
        return null;
    }


}
