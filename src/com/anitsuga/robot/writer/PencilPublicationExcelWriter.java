package com.anitsuga.robot.writer;

import com.anitsuga.robot.model.Pencil;
import com.anitsuga.robot.model.Product;

/**
 * PencilPublicationExcelWriter
 * @author agustina.dagnino
 *
 */
public class PencilPublicationExcelWriter extends PublicationExcelWriter  {

    /**
     * fields
     */
    private String[] fields = new String[] {
           "title" ,
           "type",
           "brand",
           "model",
           "unitsPerPackage",
           "isWatercolor",
           "isPastel",
           "grade"
    };
    
    /**
     * getFields
     * @return
     */
    @Override
    protected String[] getProductSpecificFields() {
        return this.fields;
    }
    
    /**
     * getProductSpecificValues
     */
    @Override
    protected String[] getProductSpecificValues( Product product ) {
        String[] ret = new String[fields.length];
        Pencil art = (Pencil) product;
        ret[0] = art.getTitle();
        ret[1] = art.getType();
        ret[2] = art.getBrand();
        ret[3] = art.getModel();
        ret[4] = art.getUnitsPerPackage();
        ret[5] = booleaValue(art.isWatercolor());
        ret[6] = booleaValue(art.isPastel());
        ret[7] = art.getGrade();
        return ret;
    }

    /**
     * booleaValue
     * @param aBool
     * @return
     */
    private String booleaValue(boolean aBool) {
        return aBool? "SI": "NO";
    }
    
}
