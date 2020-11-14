package com.anitsuga.robot;

import com.anitsuga.fwk.utils.AppProperties;
import com.anitsuga.robot.types.BookRobot;
import com.anitsuga.robot.types.FileListURLProvider;
import com.anitsuga.robot.types.MusicRobot;
import com.anitsuga.robot.types.WebScraperURLProvider;
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
            return new BookRobot();
        }

        @Override
        public String getFilename() {
            return "books";
        }
        
        @Override
        public PublicationCSVWriter getWriter() {
            return new BookPublicationExcelWriter();
        }

        @Override
        public RobotURLProvider getURLProvider() {
            AppProperties config = AppProperties.getInstance();
            String startingPointURL = config.getProperty("book.url");
            String alreadyPublishedProductsFile = config.getProperty("published.book.file");
            return new WebScraperURLProvider(startingPointURL,alreadyPublishedProductsFile);
        }

        @Override
        public boolean shouldNavigateURLs() {
            return true;
        }        
        
        @Override
        public boolean shouldRetrieveImages() {
            return true;
        }  
    },

    BOOK_ANALYZER {
        @Override
        public Robot getInstance() {
            return new BookRobot();
        }

        @Override
        public String getFilename() {
            return "books";
        }
        
        @Override
        public PublicationCSVWriter getWriter() {
            return new BookPublicationExcelWriter();
        } 
        
        @Override
        public RobotURLProvider getURLProvider() {
            AppProperties config = AppProperties.getInstance();
            String fileName = config.getProperty("book.list.file");
            return new FileListURLProvider(fileName);
        } 
        
        @Override
        public boolean shouldNavigateURLs() {
            return false;
        }        
        
        @Override
        public boolean shouldRetrieveImages() {
            return false;
        }  
    },
    
    MUSIC_SCRAPER {
        @Override
        public Robot getInstance() {
            return new MusicRobot();
        }

        @Override
        public String getFilename() {
            return "music";
        }
        
        @Override
        public PublicationCSVWriter getWriter() {
            return new MusicPublicationExcelWriter();
        } 
        
        @Override
        public RobotURLProvider getURLProvider() {
            AppProperties config = AppProperties.getInstance();
            String startingPointURL = config.getProperty("music.url");
            String alreadyPublishedProductsFile = config.getProperty("published.music.file");
            return new WebScraperURLProvider(startingPointURL,alreadyPublishedProductsFile);
        } 
        
        @Override
        public boolean shouldNavigateURLs() {
            return true;
        }      
        
        @Override
        public boolean shouldRetrieveImages() {
            return true;
        }  
    },
    
    MUSIC_ANALYZER {
        @Override
        public Robot getInstance() {
            return new MusicRobot();
        }

        @Override
        public String getFilename() {
            return "music";
        }
        
        @Override
        public PublicationCSVWriter getWriter() {
            return new MusicPublicationExcelWriter();
        } 
        
        @Override
        public RobotURLProvider getURLProvider() {
            AppProperties config = AppProperties.getInstance();
            String fileName = config.getProperty("music.list.file");
            return new FileListURLProvider(fileName);
        } 
        
        @Override
        public boolean shouldNavigateURLs() {
            return false;
        }

        @Override
        public boolean shouldRetrieveImages() {
            return false;
        }        
    }
    ;
    
    public abstract Robot getInstance();
    public abstract String getFilename();
    public abstract PublicationCSVWriter getWriter();
    public abstract RobotURLProvider getURLProvider();
    public abstract boolean shouldNavigateURLs();
    public abstract boolean shouldRetrieveImages();
    

}
