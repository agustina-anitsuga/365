package com.anitsuga.shop.api.nube.model;

import lombok.Data;

import java.util.List;

@Data
public class Image {

    private Long id;
    private Long product_id;
    private String src;
    private Integer position;
    private List<String> alt;
    private Integer height;
    private Integer width;
    private Integer thumbnails_generated;

}
