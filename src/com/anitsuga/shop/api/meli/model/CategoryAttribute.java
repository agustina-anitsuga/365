package com.anitsuga.shop.api.meli.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CategoryAttribute {

    private String id;
    private String name;

    private Map<String, Object> tags;

    private String hierarchy;
    private Integer relevance;

    private String type; // present only in some attributes (e.g. product_identifier)

    private String value_type;
    private Integer value_max_length;

    private List<AttributeValue> values;

    private List<Unit> allowed_units;
    private String default_unit;

    private String tooltip;
    private String hint;
    private String example;

    private String attribute_group_id;
    private String attribute_group_name;

}
