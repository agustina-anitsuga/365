package com.anitsuga.shop.api.nube.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Category {

    private Long id;
    private Map<String, String> name;
    private Map<String, String> description;
    private Map<String, String> handle;
    private Long parent;
    private List<Long> subcategories;
    private Map<String, String> seo_title;
    private Map<String, String> seo_description;
    private String google_shopping_category;
    private String created_at;
    private String updated_at;

}
