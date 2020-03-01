package com.robot;

import com.robot.types.BookAnalyzerRobot;
import com.robot.types.BookScraperRobot;
import com.robot.writer.BookPublicationExcelWriter;
import com.robot.writer.PublicationCSVWriter;

/**
 * RobotType
 * @author agustina.dagnino
 *
 */
public enum RobotType {

    BOOK_SCRAPER {
        @Override
        public Robot getInstance() {
            return new BookScraperRobot();
        }

        @Override
        public String getFilename() {
            return "books";
        }
        
        @Override
        public PublicationCSVWriter getWriter() {
            return new BookPublicationExcelWriter();
        } 
        
    },

    BOOK_ANALYZER {
        @Override
        public Robot getInstance() {
            return new BookAnalyzerRobot();
        }

        @Override
        public String getFilename() {
            return "books";
        }
        
        @Override
        public PublicationCSVWriter getWriter() {
            return new BookPublicationExcelWriter();
        } 
        
    };
    
    public abstract Robot getInstance();
    public abstract String getFilename();
    public abstract PublicationCSVWriter getWriter();

}
