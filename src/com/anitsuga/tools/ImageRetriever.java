package com.anitsuga.tools;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anitsuga.fwk.utils.AppProperties;
import com.anitsuga.tools.model.ImageResult;
import com.anitsuga.tools.model.Result;
import com.anitsuga.tools.page.AbeBookPage;
import com.anitsuga.tools.page.AbeSearchResultPage;
import com.anitsuga.tools.writer.ImageResultExcelWriter;
import com.anitsuga.tools.writer.ResultExcelWriter;

/**
 * ImageRetriever
 * @author agustina
 *
 */
public class ImageRetriever extends Processor {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageRetriever.class.getName());

    /**
     * ISBN_FILE
     */
    protected static final String ISBN_FILE = "book.isbn.file";

    
    /**
     * main
     * @param args
     */
    public static void main(String[] args) {
        ImageRetriever self = new ImageRetriever();
        self.run();
    }

    /**
     * outputFilePrefix
     */
    @Override
    protected String outputFilePrefix() {
        return "image-retriever";
    }

    /**
     * doProcess
     */
    @Override
    protected List<Result> doProcess(List<String> data, WebDriver driver) {
        int total = data.size();
        int count = 0;
        List<Result> ret = new ArrayList<Result>();
        for (String isbn : data) {
            LOGGER.info("Retrieving image ["+(++count)+"/"+total+"] - "+isbn);
            try {
                String result = retrieveImages(driver,isbn);
                ImageResult op = new ImageResult();
                op.setIsbn(isbn);
                op.setImages(result);
                LOGGER.info("    "+result);  
                ret.add(op);
            } catch (Exception e) {
                LOGGER.debug(e.getMessage());
            }
        }
        return ret;
    }

    /**
     * retrieveImages
     * @param driver
     * @param isbn
     * @return
     */
    private String retrieveImages(WebDriver driver, String isbn) {
        
        String result = null;
        AbeSearchResultPage searchPage = new AbeSearchResultPage(driver);
        searchPage = searchPage.go(this.getSearchUrl(isbn));
        
        List<String> urls = searchPage.getBookUrls();
        if( urls.isEmpty() ) {
            urls.add(this.getSearchUrl(isbn));
        } 
        
        for (String bookUrl : urls) {
            AbeBookPage bookPage = new AbeBookPage(driver);
            bookPage = bookPage.go(bookUrl);
            
            if( isbnMatches( isbn, bookPage.getIsbn() ) ){
                List<String> images = bookPage.getImages();
                if( (images!=null) && (images.size()>0) ){
                    result = toCommaSeparatedString(images);
                    break;
                }
            }
        }
    
        
        return result;
    }

    /**
     * isbnMatches
     * @param expectedIsbn
     * @param actualIsbn
     * @return
     */
    private boolean isbnMatches(String expectedIsbn, String actualIsbn) {
        return this.format(expectedIsbn).equals(this.format(actualIsbn));
    }

    /**
     * toCommaSeparatedString
     * @param images
     * @return
     */
    private String toCommaSeparatedString(List<String> images) {
        String ret = "";
        
        if( images.size()>0 ){
            for (String image : images) {
                ret += image+",";
            }
            ret += "https://http2.mlstatic.com/4k-ultra-hd-blu-ray-harry-potter-collection-8-films-D_NQ_NP_821117-MLA40710388896_022020-F.webp";
        }
        
        return ret;
    }

    /**
     * getSearchUrl
     * @param isbn
     * @return
     */
    private String getSearchUrl(String isbn) {
        return "https://www.abebooks.com/servlet/SearchResults?isbn="+format(isbn);
    }

    /**
     * format
     * @param isbn
     * @return
     */
    private String format(String isbn) {
        return isbn.replaceAll("-", "");
    }

    /**
     * getResultExcelWriter
     */
    @Override
    protected ResultExcelWriter getResultExcelWriter() {
        return new ImageResultExcelWriter();
    }

    /**
     * getInputFilename
     */
    @Override
    protected String getInputFilename() {
        return AppProperties.getInstance().getProperty(ISBN_FILE);
    }
   
}
