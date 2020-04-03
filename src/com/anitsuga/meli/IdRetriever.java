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
     * doProcess
     */
    @Override
    protected List<Operation> doProcess(List<Publication> data, WebDriver driver) {
        int total = data.size();
        int count = 0;
        List<Operation> ret = new ArrayList<Operation>();
        for (Publication publication : data) {
            try {
                LOGGER.info("Retrieving id ["+(++count)+"/"+total+"] - "+publication.getTitle()+" ("+publication.getIsbn()+")");
                String result = getId(driver,publication);
                Operation op = new Operation();
                op.setPublication(publication);
                op.setResult(result);
                LOGGER.info("    "+result);  
                ret.add(op);
            } catch (Exception e){
                LOGGER.error(e.getMessage());
            }
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
            return "Null title ";
        }
        
        StorePage store = new StorePage(driver).go("https://eshops.mercadolibre.com.ar/marcelofioren77");
        store.setSearch(publication.getTitle());
        
        StorePage searchResult = store.doSearch();
        searchResult.closePopups();
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
        
        if( possibleMatches.size() == 1) {
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
        String requiredIsbn = publication.getIsbn();
        String foundIsbn = page.getIsbn();
        requiredIsbn = requiredIsbn.replaceAll("-","");
        boolean ret = requiredIsbn!=null && requiredIsbn.equals(foundIsbn);
        return ret;
    }

    /**
     * titlesMatch
     * @param publication
     * @param page
     * @return
     */
    private boolean titlesMatch(Publication publication, PublicationPage page) {
        String requiredTitle = publication.getTitle();
        String foundTitle = page.getTitle();
        boolean ret = requiredTitle!=null && requiredTitle.toLowerCase().equals(foundTitle.toLowerCase());
        return ret;
    }

    /**
     * getIdFromUrl
     * @param string
     * @return
     */
    private String getIdFromUrl(String url) {
        // https://articulo.mercadolibre.com.ar/MLA-841972723-libro-...
        int from = url.indexOf("/MLA-");
        String temp = url.substring(from+1);
        int to = temp.indexOf("-",4);
        String ret = temp.substring(0,to);
        ret = ret.replaceAll("-", "");
        return ret;
    }

    /**
     * outputFilePrefix
     * @return
     */
    @Override
    protected String outputFilePrefix() {
        return "id-retriever";
    }

}
