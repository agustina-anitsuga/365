package com.anitsuga.shop.api.meli.model;

import lombok.Data;

import java.util.List;

@Data
public class SaleTerm {

    private String id;
    private String name;
    private String value_id;
    private String value_name;
    private ValueStruct value_struct;
    private List<Value> values;
    private String value_type;

}

