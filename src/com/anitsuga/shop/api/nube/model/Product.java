package com.anitsuga.shop.api.nube.model;

import lombok.Data;

import java.util.List;

@Data
public class Product extends BaseProduct {

    private List<Category> categories;

}
