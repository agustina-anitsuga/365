package com.anitsuga.robot.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anitsuga.robot.model.Book;
import com.anitsuga.robot.model.Publication;

/**
 * BookPublicationCSVWriter
 * @author agustina.dagnino
 *
 */
public class BookPublicationCSVWriter implements PublicationCSVWriter {

    
    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BookPublicationCSVWriter.class.getName());
    
    /**
     * fields
     */
    private String[] fields = new String[] {
                   "Fuente",
                   "Precio en USD",
                   "Peso",
                   "Disponibilidad",
                   "Vendido Por",
                   "Titulo",
                   "Imagenes",
                   "SKU",
                   "ISBN",
                   "Cantidad",
                   "Precio",
                   "Descripcion",
                   "Condicion",
                   "Link de Youtube",
                   "Tipo de Publicacion",
                   "Tipo de garantia",
                   "Tiempo de garantia",
                   "Unidad de tiempo de garantia",
                   "Forma de envio",
                   "Costo de envio",
                   "Retiro en persona",
                   "Tipo de narracion",
                   "Titulo",
                   "Autor",
                   "Idioma",
                   "Editorial",
                   "Formato",
                   "Tipo de tapa"
    };
    
    /**
     * write
     * @param file
     * @param publications
     */
    public void write( String filename, List<Publication> publications ) {
        
        PrintWriter out = null ;
        try {
            LOGGER.info("Writing "+publications.size()+" publications");
            
            out = new PrintWriter(new BufferedWriter(new FileWriter(filename+".csv")));
            
            for (int i = 0; i < fields.length; i++) {
                out.print(writeField(fields[i]));
            }
            out.println();
            
            for (Publication publication : publications) {
                Book book = (Book) publication.getProduct();
                String data = null;
                try {
                    data = 
                        writeField(publication.getUrl())+ // url
                        writeField(book.getPrice())+ // precio en usd
                        writeField(book.getWeight())+ // peso
                        writeField(book.getAvailability())+ // disponibilidad
                        writeField(book.getSeller())+ // vendedor
                        writeField(publication.getTitle())+ // titulo
                        writeField(toCommaSeparatedArray(publication.getImages())) + // imagenes
                        DELIMITER+ // sku
                        writeField(book.getIsbn())+ // isbn
                        writeField("1") + // Cantidad
                        writeField(publication.getPrice())+ // precio
                        writeField(publication.getDescription()) + // Descripcion
                        writeField("Nuevo") + // Condicion
                        DELIMITER + // Link de Youtube
                        writeField("Clasica") + // Tipo de Publicacion     
                        writeField("Garantia del vendedor") + //Tipo de garantia 
                        writeField("30") + // Tiempo de garantia 
                        writeField("dias") + // Unidad de tiempo de garantia
                        writeField("Mercado Envios") + // Forma de envio
                        writeField("A cargo del comprador") + // Costo de envio
                        writeField("Acepto") + // Retiro en persona
                        writeField(book.getType()) + // Tipo de narracion
                        writeField(book.getTitle()) + // titulo libro
                        writeField(book.getAuthor()) + // autor libro
                        writeField(book.getLanguage()) + // idioma
                        writeField(book.getEditorial())+ // editorial
                        writeField(book.getFormat())+ // formato
                        writeField(book.getCover()); // tapa

                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                    data = writeField(publication.getUrl());
                        
                }
                out.println(data);
                
            }
                            
            
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }  finally {
            if(out!=null){
                out.close();
            }
        }
    }

    /**
     * toCommaSeparatedArray
     * @param images
     * @return
     */
    private String toCommaSeparatedArray(List<String> images) {
        String ret = "";
        for (String image : images) {
            ret += image + ",";
        }
        return ret;
    }
    
    /**
     * writeField
     * @param data
     * @return string representing a field
     */
    public String writeField(String data) {
        return QUOTES+data+QUOTES+DELIMITER;
    }
    
}
