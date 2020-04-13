package com.anitsuga.robot;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anitsuga.fwk.utils.FileUtils;
import com.anitsuga.robot.model.Publication;
import com.anitsuga.robot.writer.PublicationCSVWriter;


/**
 * Launcher
 * @author agustina.dagnino
 *
 */
public class Launcher {

    
    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Launcher.class.getName());
    
    
    /**
     * main
     * @param args
     */
    public static void main(String[] args) {
        Launcher self = new Launcher();
        self.run(RobotType.BOOK_SCRAPER);
    }

    /**
     * run
     */
    private void run(RobotType type) {
       LOGGER.info("Starting robot "+type);
       
       // get robot
       Robot robot = type.getInstance();
    
       // validate config
       if( !robot.validConfig() ) {
           LOGGER.error("Config is invalid. Please initialize robot.properties");
           return;
       }
       
       // scrape publications
       List<Publication> publications = robot.scrape();
       
       // write them to file
       String localPath = FileUtils.getLocalPath();
       PublicationCSVWriter writer = type.getWriter();
       writer.write( localPath + type.getFilename(), publications );
       
       LOGGER.info("Finished robot "+type);
    }

    
}
