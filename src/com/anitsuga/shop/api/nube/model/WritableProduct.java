package com.anitsuga.shop.api.nube.model;

import lombok.Data;

import java.util.List;

@Data
public class WritableProduct extends Product {
    List<Long> categories;
}
