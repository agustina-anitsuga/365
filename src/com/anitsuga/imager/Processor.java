package com.anitsuga.imager;

import java.io.File;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anitsuga.fwk.utils.AppProperties;
import com.anitsuga.fwk.utils.Browser;
import com.anitsuga.fwk.utils.FileUtils;
import com.anitsuga.fwk.utils.SeleniumUtils;
import com.anitsuga.imager.model.Result;
import com.anitsuga.imager.reader.InputDataReader;
import com.anitsuga.imager.writer.ResultExcelWriter;

/**
 * Processor
 * @author agustina
 *
 */
public abstract class Processor {
    
    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class.getName());
    

    protected static final String LOCAL_PATH = "local.path";
    protected static final String ISBN_FILE = "book.isbn.file";


    /**
     * run
     */
    protected void run() {
         if ( this.validConfig() ) {
             List<String> data = this.readInputData();
             if( data.size() > 0 ){
                 WebDriver driver = SeleniumUtils.buildDriver(Browser.CHROME);
                 List<Result> result = doProcess(data, driver);
                 this.writeProcessOutput(result);
             }
         }
    }
    

    /**
     * writeProcessOutput
     * @param result
     */
    protected void writeProcessOutput(List<Result> result) {
        String localPath = FileUtils.getLocalPath();
        String filename = localPath + outputFilePrefix();
        ResultExcelWriter writer = new ResultExcelWriter(); 
        writer.write(filename, result);
    }

    /**
     * outputFilePrefix
     * @return
     */
    protected abstract String outputFilePrefix() ;
    

    /**
     * doProcess
     * @param data
     * @param driver
     * @return
     */
    protected abstract List<Result> doProcess(List<String> data, WebDriver driver) ;

    /**
     * readInputData
     * @return
     */
    protected List<String> readInputData() {
        String filename = AppProperties.getInstance().getProperty(ISBN_FILE);
        InputDataReader reader = new InputDataReader(); 
        return reader.read(filename);
    }

    /**
     * validConfig
     */
    public boolean validConfig() {
        
        boolean ret = true;
        
        AppProperties config = AppProperties.getInstance();
        
        ret &= validateFileExists(config,LOCAL_PATH);
        ret &= validateFileExists(config,ISBN_FILE);
        
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
