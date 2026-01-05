package com.anitsuga.shop.api.meli.model;

import lombok.Data;

import java.util.List;

@Data
public class Shipping {

    private String mode;
    private List<Object> methods;
    private List<String> tags;
    private String dimensions;
    private Boolean local_pick_up;
    private Boolean free_shipping;
    private String logistic_type;
    private Boolean store_pick_up;

}

