package com.anitsuga.tools.writer;

import org.apache.poi.ss.usermodel.Row;

import com.anitsuga.tools.model.Result;
import com.anitsuga.tools.model.UPCResult;

/**
 * UPCResultExcelWriter
 * @author agustina
 *
 */
public class UPCResultExcelWriter extends ResultExcelWriter {
    
    /**
     * fields
     */
    private String[] fields = new String[] {
                   "Asin",
                   "Upc",
                   "Release Year"
    };

    @Override
    protected String[] getFields() {
        return fields;
    }

    @Override
    protected void writeFieldsToRow( Result aResult, Row row) {
        int columnCount = 0;
        UPCResult result = (UPCResult) aResult;
        writeField(row,result.getAsin().toString(),columnCount++);                    
        writeField(row,result.getUpc(),columnCount++);
        writeField(row,result.getReleaseYear(),columnCount++);
    }

}
