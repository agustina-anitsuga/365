package com.anitsuga.invoicer.writer;

import java.io.FileOutputStream;
import java.io.IOException;
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

import com.anitsuga.invoicer.model.Sale;

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
       "Sale Id",
       "Title",
       "Result"
    };
    
    
    /**
     * write
     * @param filename
     * @param result
     */
    public void write( String filename, List<Sale> result ){
        
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
    private Workbook createWorkbook(List<Sale> result) throws IOException {
     
        LOGGER.info("Writing "+result.size()+" operation changes");
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
        for (Sale sale : result) {
            try 
            {
                Row row = sheet.createRow(rowCount++);
                columnCount = 0;
                if(sale!=null){
                    writeField(row,sale.getId(),columnCount++);
                    writeField(row,sale.getTitle(),columnCount++);
                    writeField(row,sale.getResult(),columnCount++);
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
