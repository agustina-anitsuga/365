package com.anitsuga.tools;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anitsuga.fwk.utils.AppProperties;
import com.anitsuga.tools.model.Result;
import com.anitsuga.tools.model.UPCResult;
import com.anitsuga.tools.page.HiperChinoPage;
import com.anitsuga.tools.writer.ResultExcelWriter;
import com.anitsuga.tools.writer.UPCResultExcelWriter;

/**
 * UPCRetriever
 * @author agustina
 *
 */
public class UPCRetriever extends Processor {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UPCRetriever.class.getName());
    
    /**
     * ASIN_FILE
     */
    protected static final String ASIN_FILE = "music.asin.file";
    
    /**
     * main
     * @param args
     */
    public static void main(String[] args) {
        UPCRetriever self = new UPCRetriever();
        self.run();
    }

    /**
     * outputFilePrefix
     */
    @Override
    protected String outputFilePrefix() {
        return "upc-retriever";
    }

    /**
     * doProcess
     */
    @Override
    protected List<Result> doProcess(List<String> data, WebDriver driver) {
        int total = data.size();
        int count = 0;
        List<Result> ret = new ArrayList<Result>();
        for (String asin : data) {
            LOGGER.info("Retrieving upc ["+(++count)+"/"+total+"] - "+asin);
            try {
                UPCResult result = retrieveUPC(driver,asin);
                LOGGER.info("    "+result.getUpc());  
                ret.add(result);
            } catch (Exception e) {
                LOGGER.debug(e.getMessage());
            }
        }
        return ret;
    }

    /**
     * retrieveUPC
     * @param driver
     * @param asin
     * @return
     */
    private UPCResult retrieveUPC(WebDriver driver, String asin) {
        
        UPCResult result = new UPCResult();
        
        HiperChinoPage page = new HiperChinoPage(driver);
        page = page.go(this.getUrlForAsin(asin));
        
        result.setAsin(asin);
        result.setUpc(page.getUpc());
        result.setReleaseYear(page.getReleaseYear());
        
        return result;
    }

    /**
     * getUrlForAsin
     * @param asin
     * @return
     */
    private String getUrlForAsin(String asin) {
        return "https://bazar.hiperchino.com/producto/"+asin;
    }

    /**
     * getResultExcelWriter
     */
    @Override
    protected ResultExcelWriter getResultExcelWriter() {
        return new UPCResultExcelWriter();
    }

    /**
     * getInputFilename
     */
    @Override
    protected String getInputFilename() {
        return AppProperties.getInstance().getProperty(ASIN_FILE);
    }
   
}
