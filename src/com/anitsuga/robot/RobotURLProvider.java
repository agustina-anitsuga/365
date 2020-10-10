package com.anitsuga.robot;

import java.util.List;

import org.openqa.selenium.WebDriver;

/**
 * RobotURLProvider
 * @author agustina
 *
 */
public interface RobotURLProvider {

     public List<String> getURLs();
       
     public void setWebDriver( WebDriver driver );

    public boolean shouldIgnoreUrl(String url);
}
