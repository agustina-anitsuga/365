package com.anitsuga.robot;

import com.anitsuga.fwk.utils.AppProperties;
import com.anitsuga.robot.types.BookRobot;
import com.anitsuga.robot.types.FileListURLProvider;
import com.anitsuga.robot.types.MovieRobot;
import com.anitsuga.robot.types.MusicRobot;
import com.anitsuga.robot.types.PencilRobot;
import com.anitsuga.robot.types.WebScraperURLProvider;
import com.anitsuga.robot.writer.BookPublicationExcelWriter;
import com.anitsuga.robot.writer.MoviePublicationExcelWriter;
import com.anitsuga.robot.writer.MusicPublicationExcelWriter;
import com.anitsuga.robot.writer.PencilPublicationExcelWriter;
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
    },
    
    PENCIL_ANALYZER {
        @Override
        public Robot getInstance() {
            return new PencilRobot();
        }

        @Override
        public String getFilename() {
            return "pencil";
        }
        
        @Override
        public PublicationCSVWriter getWriter() {
            return new PencilPublicationExcelWriter();
        } 
        
        @Override
        public RobotURLProvider getURLProvider() {
            AppProperties config = AppProperties.getInstance();
            String fileName = config.getProperty("pencil.list.file");
            return new FileListURLProvider(fileName);
        } 
        
        @Override
        public boolean shouldNavigateURLs() {
            return false;
        }

        @Override
        public boolean shouldRetrieveImages() {
            return true;
        }        
    },
    
    MOVIE_ANALYZER {
        @Override
        public Robot getInstance() {
            return new MovieRobot();
        }

        @Override
        public String getFilename() {
            return "movie";
        }
        
        @Override
        public PublicationCSVWriter getWriter() {
            return new MoviePublicationExcelWriter();
        } 
        
        @Override
        public RobotURLProvider getURLProvider() {
            AppProperties config = AppProperties.getInstance();
            String fileName = config.getProperty("movie.list.file");
            return new FileListURLProvider(fileName);
        } 
        
        @Override
        public boolean shouldNavigateURLs() {
            return false;
        }

        @Override
        public boolean shouldRetrieveImages() {
            return true;
        }        
        
        public boolean dropShippingEnabled(){
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
    
    public boolean dropShippingEnabled() { return true; }

}
