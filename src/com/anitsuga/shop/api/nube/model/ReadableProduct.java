package com.anitsuga.shop.api.nube.model;

import lombok.Data;

import java.util.List;

@Data
public class ReadableProduct extends Product {

    private List<Category> categories;

}
