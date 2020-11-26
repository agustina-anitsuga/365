package com.anitsuga.robot.model;

/**
 * Pencil
 * @author agustina
 *
 */
public class Pencil extends Product {
 
    private String title;
    private String type; // color / grafito
    private String brand;
    private String model;
    private String unitsPerPackage;
    private boolean isWatercolor;
    private boolean isPastel;
    private String grade; // HB, 2B, etc
    
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getUnitsPerPackage() {
        return unitsPerPackage;
    }

    public void setUnitsPerPackage(String unitsPerPackage) {
        this.unitsPerPackage = unitsPerPackage;
    }

    public boolean isWatercolor() {
        return isWatercolor;
    }

    public void setWatercolor(boolean isWatercolor) {
        this.isWatercolor = isWatercolor;
    }

    public boolean isPastel() {
        return isPastel;
    }

    public void setPastel(boolean isPastel) {
        this.isPastel = isPastel;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
    
}
