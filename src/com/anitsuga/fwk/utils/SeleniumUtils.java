package com.anitsuga.fwk.utils;

import java.io.File;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anitsuga.fwk.exception.FrontEndException;


/**
 * SeleniumUtils
 * 
 * @author agustina.dagnino
 *
 */
public class SeleniumUtils {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SeleniumUtils.class.getName());
    
    
    /**
     * Private Constructor
     */
    private SeleniumUtils() {
        // class should not be instantiated
    }

    /**
     * buildDriver
     * 
     * @param browser
     * @return driver
     * @throws Exception
     */
    public static WebDriver buildDriver(Browser browser) throws FrontEndException {
        WebDriver driver = null;

        switch (browser) {
        case FIREFOX:
            driver = new FirefoxDriver();
            break;
        case CHROME:
            String path = "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome";
            ChromeOptions options = new ChromeOptions();
            options.setBinary(path);
            driver = new ChromeDriver(options);
            break;
        case IEXPLORE:
            driver = new InternetExplorerDriver();
            break;
        case HTML_UNIT:
            driver = new HtmlUnitDriver();
            break;

        default:
            throw new FrontEndException("Browser not found!");
        }
        
        driver.manage().window().maximize();
        return driver;
    }
    
    /**
     * getWait
     * @param driver
     * @return
     */
    public static WebDriverWait getWait(WebDriver driver) {
        Duration duration = Duration.ofSeconds(3);
        WebDriverWait webDriverWait = new WebDriverWait(driver,duration);
        return webDriverWait;
    }

    /**
     * getWait
     * @param driver
     * @return
     */
    public static WebDriverWait getWait(WebDriver driver, int maxWait ) {
        java.time.Duration duration = Duration.ofSeconds(maxWait);
        return new WebDriverWait(driver, duration);
    }
    
    /**
     * captureScreenshot
     * @param driver
     */
    public static void captureScreenshot(WebDriver driver) {
        if( isScreenshotEnabled() )
        try {
            // take screenshot and save it to file
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File fileDestiny = new File("output/robot-"+System.currentTimeMillis()+".jpg");
            FileUtils.copyFile(screenshot, fileDestiny);
            LOGGER.info("Screenshot generated in: " + fileDestiny.getAbsolutePath());
        } catch (Exception ioe) {
            LOGGER.info("Could not generate screenshot",ioe);
        }
    }

    /**
     * isScreenshotEnabled
     * @return
     */
    private static boolean isScreenshotEnabled() {
        return false;
    }
}
