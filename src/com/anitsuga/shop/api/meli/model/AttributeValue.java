package com.anitsuga.shop.api.meli.model;

import lombok.Data;

import java.util.Map;

@Data
public class AttributeValue {

    private String id;
    private String name;

    // Used in boolean attributes and others
    private Map<String, Object> metadata;

    // Used in number_unit attributes in some APIs
    private ValueStruct struct;

}

