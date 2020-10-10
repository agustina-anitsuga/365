package com.anitsuga.robot;

import java.util.List;

import com.anitsuga.robot.model.Publication;


/**
 * Robot
 * @author agustina.dagnino
 *
 */
public interface Robot {

    /**
     * scrape
     * @param resultFile
     */
    public List<Publication> scrape();
 
    /**
     * validConfig
     * @return
     */
    public boolean validConfig();

    /**
     * setURLProvider
     * @param urlProvider
     */
    public void setURLProvider(RobotURLProvider urlProvider);

    /**
     * shouldNavigateURLs
     * @param shouldNavigateURLs
     */
    public void shouldNavigateURLs(boolean shouldNavigateURLs);

}
