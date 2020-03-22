package com.robot.page;

import org.openqa.selenium.WebDriver;

/**
 * LoginPage
 * @author agustina
 *
 */
public class LoginPage extends Page {

    public LoginPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * go
     * @param url
     * @return
     */
    public LoginPage go(String url) {
        // go to url
        driver.get(url);

        // return page
        return new LoginPage(this.driver);
    }    

}
