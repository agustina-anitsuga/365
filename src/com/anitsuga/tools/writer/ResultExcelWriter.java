package com.anitsuga.tools.writer;

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

import com.anitsuga.tools.model.Result;

/**
 * ResultExcelWriter
 * @author agustina
 *
 */
public abstract class ResultExcelWriter {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ResultExcelWriter.class.getName());
    
    
    /**
     * write
     * @param filename
     * @param result
     */
    public void write( String filename, List<Result> result ){
        
        FileOutputStream outputStream = null;
        Workbook workbook = null;
        
        try {
            workbook = this.createWorkbook(result);
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
     * createWorkbook
     * @param result
     * @return
     * @throws IOException 
     */
    private Workbook createWorkbook(List<Result> result) throws IOException {
     
        LOGGER.info("Writing "+result.size()+" results");
        int rowCount = 0;
        int columnCount=0;
        
        Workbook workbook = WorkbookFactory.create(true);
        Sheet sheet = workbook.createSheet("result");
         
        // write header
        String[] fields = getFields();
        Row headerRow = sheet.createRow(rowCount++);
        for (int i = 0; i < fields.length; i++) {
            CellStyle cellstyle = getHeaderFieldsStyle(workbook);
            Cell cell = headerRow.createCell(columnCount++);
            cell.setCellValue(fields[i]);
            cell.setCellStyle(cellstyle);
        }
        
        // write data
        for (Result aResult : result) {
            try 
            {
                Row row = sheet.createRow(rowCount++);
                writeFieldsToRow(aResult, row);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
        
        LOGGER.info("Done writing "+result.size()+" results");        
        return workbook;
    }

    protected abstract String[] getFields() ;

    protected abstract void writeFieldsToRow(Result aResult, Row row);

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
    protected void writeField(Row row, String data, int columnCount) {
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
