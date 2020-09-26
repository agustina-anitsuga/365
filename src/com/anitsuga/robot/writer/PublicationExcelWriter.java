package com.anitsuga.robot.writer;

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

import com.anitsuga.robot.model.Product;
import com.anitsuga.robot.model.Publication;

/**
 * PublicationExcelWriter
 * @author agustina.dagnino
 *
 */
public abstract class PublicationExcelWriter implements PublicationCSVWriter {

    
    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PublicationExcelWriter.class.getName());
    
    /**
     * fields
     */
    private String[] genericFields = new String[] {
                   "Amazon ID",   
                   "Fuente",
                   "Precio en USD",
                   "Peso",
                   "Peso en Kilos",
                   "Disponibilidad",
                   "Seller",
                   "Título",
                   "Codigo Universal (ISBN u otros)",                   
                   "Imágenes",
                   "SKU",
                   "Cantidad",
                   "Precio",
                   "Condición",
                   "Descripción",
                   "Link de Youtube",
                   "Tipo de Publicación",
                   "Forma de envío",
                   "Costo de envío",
                   "Retiro en persona",
                   "Tipo de garantía",
                   "Tiempo de garantía",
                   "Unidad de tiempo de garantía"
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
            String[] fields = this.getFields();
            Row headerRow = sheet.createRow(rowCount++);
            for (int i = 0; i < fields.length; i++) {
                CellStyle cellstyle = (i<7)? getExtraFieldsStyle(workbook) : getRequiredFieldsStyle(workbook);
                Cell cell = headerRow.createCell(columnCount++);
                cell.setCellValue(fields[i]);
                cell.setCellStyle(cellstyle);
            }
            
            // write data
            for (Publication publication : publications) {
                try 
                {
                    Row row = sheet.createRow(rowCount);
                    columnCount = 0;
                    Product product = (Product) publication.getProduct();
                    if(product!=null){
                        writeField(row,product.getAmazonID(),columnCount++);
                        writeField(row,publication.getUrl(),columnCount++);
                        writeField(row,product.getPrice(),columnCount++); // precio en usd
                        writeField(row,product.getWeight(),columnCount++); // peso
                        writeField(row,(product.getWeightInKilos() == null )?"":product.getWeightInKilos().toString(),columnCount++); // peso
                        writeField(row,product.getAvailability(),columnCount++); // disponibilidad
                        writeField(row,product.getSeller(),columnCount++); // seller   
                        writeField(row,publication.getTitle(),columnCount++); // titulo
                        writeField(row,product.getGenericIdentifier(),columnCount++); // isbn
                        writeField(row,toCommaSeparatedArray(publication.getImages()),columnCount++); // imagenes
                        writeField(row,"",columnCount++); // sku
                        writeField(row,"1",columnCount++); // Cantidad
                        writeField(row,publication.getPrice(),columnCount++); // precio
                        writeField(row,"Nuevo",columnCount++); // Condicion
                        writeField(row,publication.getDescription(),columnCount++); // Descripcion
                        writeField(row,"",columnCount++); // Link de Youtube
                        writeField(row,"Clásica",columnCount++); // Tipo de Publicacion     
                        writeField(row,"Mercado Envíos",columnCount++); // Forma de envio
                        writeField(row,"A cargo del comprador",columnCount++) ; // Costo de envio
                        writeField(row,"Acepto",columnCount++); // Retiro en persona
                        writeField(row,"Garantía del vendedor",columnCount++); //Tipo de garantia 
                        writeField(row,"30",columnCount++); // Tiempo de garantia 
                        writeField(row,"días",columnCount++); // Unidad de tiempo de garantia
                        String[] productSpecificValues = this.getProductSpecificValues(product);
                        for (String productSpecificValue : productSpecificValues) {
                            writeField(row,productSpecificValue,columnCount++); 
                        }    
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
     * getProductSpecificValues
     * @return
     */
    protected abstract String[] getProductSpecificValues( Product product ) ;

    /**
     * getProductSpecificFields
     * @return
     */
    protected abstract String[] getProductSpecificFields() ;

    /**
     * getFields
     * @return
     */
    protected String[] getFields() {
        String[] productSpecificFields = this.getProductSpecificFields();
        int size = this.genericFields.length + productSpecificFields.length;
        String[] result = new String[size];
        int index = 0 ;
        for (int i = 0; i < genericFields.length; i++, index++) {
            result[index]=genericFields[i];
        }
        for (int i = 0; i < productSpecificFields.length; i++, index++) {
            result[index]=productSpecificFields[i];
        }
        return result;
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
