package com.anitsuga.shop.api.nube.model;

import lombok.Data;

import java.util.List;

@Data
public class NewProduct extends BaseProduct {
    List<Long> categories;
}
