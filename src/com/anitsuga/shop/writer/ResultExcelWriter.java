package com.anitsuga.shop.writer;

import com.anitsuga.shop.model.Listing;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * ResultExcelWriter
 * @author agustina
 *
 */
public class ResultExcelWriter {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ResultExcelWriter.class.getName());
    
    
    /**
     * fields
     */
    private String[] fields = new String[] {
       "Listing Id",
       "Title",
       "Result"
    };
    
    
    /**
     * write
     * @param filename
     * @param result
     */
    public void write( String filename, List<Listing> result ){
        
        FileOutputStream outputStream = null;
        Workbook workbook = null;
        
        try {
            workbook = createWorkbook(result);
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
                    LOGGER.error(e.getMessage()+"");
                }
            }
        }
        
    }
    
    /**
     * createWorkbook
     * @param result
     * @return
     * @throws IOException 
     */
    private Workbook createWorkbook(List<Listing> result) throws IOException {
     
        LOGGER.info("Writing "+result.size()+" listing changes");
        int rowCount = 0;
        int columnCount=0;
        
        Workbook workbook = WorkbookFactory.create(true);
        Sheet sheet = workbook.createSheet("result");
         
        // write header
        Row headerRow = sheet.createRow(rowCount++);
        for (int i = 0; i < fields.length; i++) {
            CellStyle cellstyle = getHeaderFieldsStyle(workbook);
            Cell cell = headerRow.createCell(columnCount++);
            cell.setCellValue(fields[i]);
            cell.setCellStyle(cellstyle);
        }
        
        // write data
        for (Listing listing : result) {
            try 
            {
                Row row = sheet.createRow(rowCount++);
                columnCount = 0;
                if(listing!=null){
                    writeField(row,listing.getId(),columnCount++);
                    writeField(row,listing.getTitle(),columnCount++);
                    writeField(row,listing.getResult(),columnCount++);
                } 
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
        
        LOGGER.info("Done writing "+result.size()+" operation changes");        
        return workbook;
    }

    /**
     * getHeaderFieldsStyle
     * @param wb
     * @return
     */
    private CellStyle getHeaderFieldsStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GOLD.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }
    
    /**
     * writeField
     * @param data
     */
    public void writeField(Row row, String data, int columnCount) {
        Cell cell = row.createCell(columnCount);
        cell.setCellValue(data);
    }
    
    /**
     * getFullFilename
     * @param filename
     * @return
     */
    private String getFullFilename(String filename) {
        return filename+"-"+System.currentTimeMillis()+".xlsx";
    }
}
