package com.anitsuga.shop.api.nube.model;

import lombok.Data;

import java.util.List;

@Data
public class Variant {

    private Long id;
    private Long image_id;
    private Long product_id;
    private Integer position;
    private String price;
    private String compare_at_price;
    private String promotional_price;
    private Boolean stock_management;
    private Integer stock;
    private String weight;
    private String width;
    private String height;
    private String depth;
    private String sku;
    private List<Object> values;
    private String barcode;
    private String mpn;
    private String age_group;
    private String gender;
    private String created_at;
    private String updated_at;
    private String cost;
    private Boolean visible;
    private List<InventoryLevel> inventory_levels;
    private boolean success;
}
