package com.anitsuga.shop.api.nube.model;

import lombok.Data;

@Data
public class InventoryLevel {

    private Long id;
    private Long variant_id;
    private String location_id;
    private Integer stock;

}
