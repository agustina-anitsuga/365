package com.anitsuga.meli.reader;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anitsuga.meli.model.Publication;

/**
 * InputDataReader
 * @author agustina
 *
 */
public class InputDataReader {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(InputDataReader.class.getName());
    
    
    /**
     * read
     * @param filename
     * @return
     */
    public List<Publication> read(String filename) {

        List<Publication> data = new ArrayList<Publication>();
        Workbook workbook = null;
        Publication publication = null;
                
        try {
            FileInputStream excelFile = new FileInputStream(new File(filename));
            workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();
            int rowCount = 0;
            
            while (iterator.hasNext()) {
                Row currentRow = iterator.next();
                if( rowCount > 0 ){
                    
                    String title = getTitle(currentRow);
                    if( !StringUtils.isEmpty(title) ){
                        String id = getId(currentRow);
                        Number price = getPrice(currentRow);
                        String isbn = getISBN(currentRow);
                        publication = new Publication();
                        publication.setId(id);
                        publication.setTitle(title);
                        publication.setPrice(price);
                        publication.setIsbn(isbn);
                        data.add(publication);
                    }
                }
                rowCount++;
            }
        
        } catch (Exception e) {
            LOGGER.error(publication.getId()+" "+e.getMessage());
            data = new ArrayList<Publication>();
        } finally {
            try {
            workbook.close();
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
        
        // data.addAll(getSampleData());
        
        return data;
    }

    /**
     * getISBN
     * @param currentRow
     * @return
     */
    private String getISBN(Row currentRow) {
        Cell cell = currentRow.getCell(11);
        return cell.getStringCellValue();
    }

    /**
     * getPrice
     * @param currentRow
     * @return
     */
    private Number getPrice(Row currentRow) {
        Number ret = null;
        Cell cell = currentRow.getCell(13);
        CellType type = cell.getCellType();
        if( type == CellType.NUMERIC ) {
            ret = cell.getNumericCellValue();
        } else {
            String value = cell.getStringCellValue();
            value = value.replace(",", ".");
            ret = Double.valueOf(value);
        }
        return ret;
    }

    /**
     * getTitle
     * @param currentRow
     * @return
     */
    private String getTitle(Row currentRow) {
        Cell cell = currentRow.getCell(8);
        return cell.getStringCellValue();
    }

    /**
     * getId
     * @param currentRow
     * @return
     */
    private String getId(Row currentRow) {
        Cell cell = currentRow.getCell(2);
        return cell.getStringCellValue();
    }

    /**
     * getSampleData
     * @return
     */
    private List<Publication> getSampleData() {
        
        List<Publication> data = new ArrayList<Publication>();
        
        Publication publication = new Publication();
        publication.setId("MLA845929856");
        publication.setPrice(82);
        publication.setTitle("Libro - Prueba - No Comprar");
        publication.setIsbn("1234");
        data.add(publication);
        
        publication = new Publication();
        publication.setId("MLA845929856");
        publication.setPrice(23);
        publication.setTitle("Libro - Prueba - No Comprar");
        publication.setIsbn("1234");
        data.add(publication);

        publication = new Publication();
        publication.setId("MLA845929856");
        publication.setPrice(82);
        publication.setTitle("Libro - Prueba - No Comprar");
        publication.setIsbn("1234");
        data.add(publication);
        
        publication = new Publication();
        publication.setId("MLA845929856");
        publication.setPrice(23);
        publication.setTitle("Libro - Prueba - No Comprar");
        publication.setIsbn("1234");
        data.add(publication);
        
        return data;
    }

}
