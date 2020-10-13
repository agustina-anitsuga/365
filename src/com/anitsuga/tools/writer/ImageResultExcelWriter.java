package com.anitsuga.tools.writer;

import org.apache.poi.ss.usermodel.Row;

import com.anitsuga.tools.model.ImageResult;
import com.anitsuga.tools.model.Result;

/**
 * ImageResultExcelWriter
 * @author agustina
 *
 */
public class ImageResultExcelWriter extends ResultExcelWriter {
    
    /**
     * fields
     */
    private String[] fields = new String[] {
                   "ISBN",
                   "Images"
    };

    @Override
    protected String[] getFields() {
        return fields;
    }

    @Override
    protected void writeFieldsToRow( Result aResult, Row row) {
        int columnCount = 0;
        ImageResult result = (ImageResult) aResult;
        writeField(row,result.getIsbn().toString(),columnCount++);                    
        writeField(row,result.getImages(),columnCount++);
    }

}
