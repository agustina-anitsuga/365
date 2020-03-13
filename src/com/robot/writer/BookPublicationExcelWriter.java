package com.robot.writer;

import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.robot.model.Book;
import com.robot.model.Publication;

/**
 * BookPublicationCSVWriter
 * @author agustina.dagnino
 *
 */
public class BookPublicationExcelWriter implements PublicationCSVWriter {

    
    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BookPublicationExcelWriter.class.getName());
    
    /**
     * fields
     */
    private String[] fields = new String[] {
                   "Fuente",
                   "Precio en USD",
                   "Peso",
                   "Disponibilidad",
                   "Seller",
                   "Título",
                   "Imágenes",
                   "SKU",
                   "ISBN",
                   "Cantidad",
                   "Precio",
                   "Descripción",
                   "Condición",
                   "Link de Youtube",
                   "Tipo de Publicación",
                   "Tipo de garantía",
                   "Tiempo de garantía",
                   "Unidad de tiempo de garantía",
                   "Forma de envío",
                   "Costo de envío",
                   "Retiro en persona",
                   "Tipo de narración",
                   "Título",
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
        
        FileOutputStream outputStream = null;
        Workbook workbook = null;
        
        try {
            
            LOGGER.info("Writing "+publications.size()+" publications");
            int rowCount = 0;
            int columnCount=0;
            
            workbook = WorkbookFactory.create(true);
            Sheet sheet = workbook.createSheet("books");
             
            // write header
            Row headerRow = sheet.createRow(rowCount++);
            for (int i = 0; i < fields.length; i++) {
                CellStyle cellstyle = (i<5)? getExtraFieldsStyle(workbook) : getRequiredFieldsStyle(workbook);
                Cell cell = headerRow.createCell(columnCount++);
                cell.setCellValue(fields[i]);
                cell.setCellStyle(cellstyle);
            }
            
            System.out.println("");
            
            // write data
            for (Publication publication : publications) {
                try 
                {
                    Row row = sheet.createRow(rowCount);
                    columnCount = 0;
                    Book book = (Book) publication.getProduct();
                    if(book!=null){
                        writeField(row,publication.getUrl(),columnCount++);
                        writeField(row,book.getPrice(),columnCount++); // precio en usd
                        writeField(row,book.getWeight(),columnCount++); // peso
                        writeField(row,book.getAvailability(),columnCount++); // disponibilidad
                        writeField(row,book.getSeller(),columnCount++); // seller   
                        writeField(row,publication.getTitle(),columnCount++); // titulo
                        writeField(row,toCommaSeparatedArray(publication.getImages()),columnCount++); // imagenes
                        writeField(row,"",columnCount++); // sku
                        writeField(row,book.getIsbn(),columnCount++); // isbn
                        writeField(row,"1",columnCount++); // Cantidad
                        writeField(row,publication.getPrice(),columnCount++); // precio
                        writeField(row,publication.getDescription(),columnCount++); // Descripcion
                        writeField(row,"Nuevo",columnCount++); // Condicion
                        writeField(row,"",columnCount++); // Link de Youtube
                        writeField(row,"Clásica",columnCount++); // Tipo de Publicacion     
                        writeField(row,"Garantía del vendedor",columnCount++); //Tipo de garantia 
                        writeField(row,"30",columnCount++); // Tiempo de garantia 
                        writeField(row,"días",columnCount++); // Unidad de tiempo de garantia
                        writeField(row,"Mercado Envíos",columnCount++); // Forma de envio
                        writeField(row,"A cargo del comprador",columnCount++) ; // Costo de envio
                        writeField(row,"Acepto",columnCount++); // Retiro en persona
                        writeField(row,book.getType(),columnCount++); // Tipo de narracion
                        writeField(row,book.getTitle(),columnCount++); // titulo libro
                        writeField(row,book.getAuthor(),columnCount++); // autor libro
                        writeField(row,book.getLanguage(),columnCount++); // idioma
                        writeField(row,book.getEditorial(),columnCount++); // editorial
                        writeField(row,book.getFormat(),columnCount++); // formato
                        writeField(row,book.getCover(),columnCount++); // tapa       
                        rowCount++;
                    } else {
                        LOGGER.error("Empty book for URL "+publication.getUrl());
                    }
                } catch(Exception e) {
                    LOGGER.error(e.getMessage());
                    e.printStackTrace();
                }
            }
                            
            String fullFilename =getFullFilename(filename);
            outputStream = new FileOutputStream(fullFilename);
            workbook.write(outputStream);
            
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }  finally {
            if(outputStream!=null){
                try {
                    outputStream.close();
                } catch (Exception e) { 
                    LOGGER.error(e.getMessage());
                }
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
     * getRequiredFieldsStyle
     * @param wb
     * @return
     */
    private CellStyle getRequiredFieldsStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    /**
     * getExtraFieldsStyle
     * @param wb
     * @return
     */
    private CellStyle getExtraFieldsStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GOLD.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    /**
     * getFullFilename
     * @param filename
     * @return
     */
    private String getFullFilename(String filename) {
        return filename+"-"+System.currentTimeMillis()+".xlsx";
    }
 
    /**
     * writeField
     * @param data
     */
    public void writeField(Row row, String data, int columnCount) {
        Cell cell = row.createCell(columnCount);
        cell.setCellValue(data);
    }
    
}
