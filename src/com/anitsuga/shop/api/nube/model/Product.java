package com.anitsuga.shop.api.nube.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Product {
    private Long id;
    private Map<String, String> name;
    private Map<String, String> description;
    private Map<String, String> handle;
    private List<Object> attributes;
    private Boolean published;
    private Boolean free_shipping;
    private Boolean requires_shipping;
    private String canonical_url;
    private String video_url;
    private Map<String, String> seo_title;
    private Map<String, String> seo_description;
    private String brand;
    private List<Variant> variants;
    private String tags;
    private List<Image> images;
    private String created_at;
    private String updated_at;
}
