package com.anitsuga.shop.api.meli.model;

import lombok.Data;

@Data
public class SearchLocation {

    private IdentifiedEntity neighborhood;
    private IdentifiedEntity city;
    private IdentifiedEntity state;

}
