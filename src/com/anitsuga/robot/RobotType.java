package com.anitsuga.robot;

import com.anitsuga.robot.types.BookAnalyzerRobot;
import com.anitsuga.robot.types.BookScraperRobot;
import com.anitsuga.robot.types.MusicScraperRobot;
import com.anitsuga.robot.writer.BookPublicationExcelWriter;
import com.anitsuga.robot.writer.MusicPublicationExcelWriter;
import com.anitsuga.robot.writer.PublicationCSVWriter;

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
        
    },
    
    MUSIC_SCRAPER {
        @Override
        public Robot getInstance() {
            return new MusicScraperRobot();
        }

        @Override
        public String getFilename() {
            return "music";
        }
        
        @Override
        public PublicationCSVWriter getWriter() {
            return new MusicPublicationExcelWriter();
        } 
        
    }
    ;
    
    public abstract Robot getInstance();
    public abstract String getFilename();
    public abstract PublicationCSVWriter getWriter();

}
