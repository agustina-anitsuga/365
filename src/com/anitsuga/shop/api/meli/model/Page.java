package com.anitsuga.shop.api.meli.model;

import lombok.Data;

import java.util.List;

@Data
public class Page {
    List<String> results;
    String scroll_id;
}
