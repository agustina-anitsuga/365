package com.robot;

import java.util.List;

import com.robot.model.Publication;


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
}
