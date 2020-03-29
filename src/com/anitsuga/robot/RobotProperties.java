package com.anitsuga.robot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RobotProperties
 * @author agustina.dagnino
 *
 */
public class RobotProperties {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RobotProperties.class.getName());

    /**
     * config
     */
    private static java.util.Properties config = new java.util.Properties();
    
    /**
     * instance
     */
    private static RobotProperties instance = null;
    
    
    /**
     * getInstance
     * @return
     */
    public static RobotProperties getInstance(){
        if(instance==null){
            instance = new RobotProperties();
        }
        return instance;
    }
    
    /**
     * Constructor
     */
    private RobotProperties() {
        try {
            config.load(this.getClass().getClassLoader().getResourceAsStream("robot.properties"));
        } catch (Exception e) {
            LOGGER.error("Could not read properties file.", e);
        }
    }
    
    /**
     * getProperty
     * @param property
     * @return
     */
    public String getProperty(String property){
        return config.getProperty(property);
    }

}
