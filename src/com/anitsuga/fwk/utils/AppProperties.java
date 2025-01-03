package com.anitsuga.fwk.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;

/**
 * AppProperties
 * @author agustina.dagnino
 *
 */
public class AppProperties {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AppProperties.class.getName());

    /**
     * config
     */
    private static java.util.Properties config = new java.util.Properties();
    
    /**
     * instance
     */
    private static AppProperties instance = null;
    
    
    /**
     * getInstance
     * @return
     */
    public static AppProperties getInstance(){
        if(instance==null){
            instance = new AppProperties();
        }
        return instance;
    }
    
    /**
     * Constructor
     */
    private AppProperties() {
        try {
            config.load(new FileReader("resources/app.properties"));
            //config.load(this.getClass().getClassLoader().getResourceAsStream("app.properties"));
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

    /**
     * getBooleanProperty
     * @param property
     * @return
     */
    public boolean getBooleanProperty(String property){
        return Boolean.parseBoolean(config.getProperty(property));
    }
}
