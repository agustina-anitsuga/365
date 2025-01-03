package com.anitsuga.meli;

import java.io.File;
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
import com.anitsuga.meli.page.PublicationEditPage;
import com.anitsuga.meli.reader.InputDataReader;
import com.anitsuga.meli.writer.ResultExcelWriter;

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
    protected static final String INPUT_FILE = "input.file";


    /**
     * run
     */
    protected void run() {
         if ( this.validConfig() ) {
             List<Publication> data = this.readInputData();
             if( data.size() > 0 ){
                 WebDriver driver = SeleniumUtils.buildDriver(Browser.CHROME);
                 List<Operation> result = doProcess(data, driver);
                 this.writeProcessOutput(result);
             }
         }
    }
    

    /**
     * writeProcessOutput
     * @param result
     */
    protected void writeProcessOutput(List<Operation> result) {
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
    protected abstract List<Operation> doProcess(List<Publication> data, WebDriver driver) ;

    /**
     * readInputData
     * @return
     */
    protected List<Publication> readInputData() {
        String filename = AppProperties.getInstance().getProperty(INPUT_FILE);
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

    /**
     * goToEditPage
     * @param driver
     * @param publication
     * @return
     */
    protected PublicationEditPage goToEditPage(WebDriver driver, Publication publication) {
        String editUrl = "https://www.mercadolibre.com.ar/publicaciones/"+publication.getId()+"/modificar";
        PublicationEditPage publicationPage = new PublicationEditPage(driver).go(editUrl);
        publicationPage.waitForLoad();
        return publicationPage;
    }

    /**
     * login
     */
    protected void login( WebDriver driver ){
        String url = "https://www.mercadolibre.com.ar/";
        LoginPage page = new LoginPage(driver);
        page.go(url);
        waitForInput();
    }

    /**
     * waitForInput
     */
    protected void waitForInput() {
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
     * titlesMatch
     * @param publication
     * @param publicationPage
     * @return
     */
    protected boolean titlesMatch(Publication publication, PublicationEditPage publicationPage) {
        String desiredTitle = cleanTitle(publication.getTitle());
        String actualTitle = cleanTitle(publicationPage.getTitle());
        return desiredTitle.equals(actualTitle);
    }

    /**
     * cleanTitle
     * @param title
     * @return
     */
    private String cleanTitle(String title) {
        title = title.toLowerCase();
        title = title.replaceAll("'", "");
        title = title.replaceAll(":", "");
        title = title.replaceAll("\\.", "");
        title = title.replaceAll("\\(", "");
        title = title.replaceAll("\\)", "");
        title = title.replaceAll("\\?", "");
        title = title.replaceAll("’", "");
        title = title.replaceAll("–", "");
        title = title.replaceAll("\"", "");
        title = title.replaceAll("á", "a");
        title = title.replaceAll("é", "e");
        title = title.replaceAll("í", "i");
        title = title.replaceAll("ó", "o");
        title = title.replaceAll("ú", "u");
        title = title.replaceAll("ü", "u");
        title = title.replaceAll(",", "");
        title = title.replaceAll("/", "");
        title = title.replaceAll("¿", "");
        return title;
    }


}
