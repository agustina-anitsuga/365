package com.anitsuga.robot.types;

import org.junit.Assert;
import org.junit.Test;

import com.anitsuga.robot.model.Book;
import com.anitsuga.robot.types.BookRobot;
import com.anitsuga.robot.types.BookScraperRobot;


/**
 * BookRobotTest
 * @author agustina
 *
 */
public class BookRobotTest {

    /**
     * testTitleGeneration
     */
    @Test
    public void testTitleGeneration(){
        String[][] testCases = new String[][]{
            {"The Sword of Truth, Boxed Set III, Books 7-9: The Pillars of Creation, Naked Empire, Chainfire","Terry Goodkind","Libro The Sword of Truth, Boxed Set III, Books 7-9: The ..."},
            {"Wheel of Time Premium Boxed Set III: Books 7-9 (A Crown of Swords, The Path of Daggers, Winter's Heart)","Robert Jordan","Libro Wheel of Time Premium Boxed Set III: Books 7-9 ..."},
            {"Wheel of Time Premium Boxed Set I: Books 1-3 (The Eye of the World, The Great Hunt, The Dragon Reborn)","Robert Jordan","Libro Wheel of Time Premium Boxed Set I: Books 1-3 ..."},
            {"Wheel of Time Premium Boxed Set II: Books 4-6 (The Shadow Rising, The Fires of Heaven, Lord of Chaos)","Robert Jordan","Libro Wheel of Time Premium Boxed Set II: Books 4-6 ..."},
            {"The Chronicles of Narnia","C.S.Lewis", "Libro The Chronicles of Narnia / C.S.Lewis"},
            {"The Chronicles of Narnia with no room for author","C.S.Lewis", "Libro The Chronicles of Narnia with no room for author"},
            {"The Chronicles of Narnia with a very long title over 60 characters","C.S.Lewis", "Libro The Chronicles of Narnia with a very long title ..."}, 
            {"Little Black Classics Box Set (Penguin Little Black Classics)", "Various", "Libro Little Black Classics Box Set /Penguin Little ..."},
            {"El monstruo mundo (Colección Ríolago) Spanish Edition","Azucena Hernández","Libro El monstruo mundo (Colección Ríolago)"},
            {"La hora muerta (Crónicas del Homo mortem) Spanish Ed.","Vicente Silvestre Marco","Libro La hora muerta (Crónicas del Homo mortem)"},
            {"Dreams: El tablero de los sueños (Isarois) Spanish Ed","Óscar A. Nchaso","Libro Dreams: El tablero de los sueños (Isarois)"},
            {"La Montaña Mágica: Spanish Version (Spanish Edition)","Thomas Mann","Libro La Montaña Mágica / Thomas Mann"},
            {"El niño Astronauta: La busqueda de un amigo / Spanish","Jorge Luis Gómez","Libro El niño Astronauta: La busqueda de un amigo"}
        };
        
        BookRobot robot = new BookScraperRobot();
        for (String[] testCase : testCases) {
            Book book = new Book();
            book.setTitle(testCase[0]);
            book.setAuthor(testCase[1]);
            String actualTitle = robot.getPublicationTitle(book);
            String expectedTitle = testCase[2];
            boolean result = actualTitle.equals(expectedTitle);
            Assert.assertTrue(result);
        }
    }
}
