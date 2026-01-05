package com.anitsuga.shop.api.meli.model;

import lombok.Data;

@Data
public class SellerAddress {

    private String comment;
    private String address_line;
    private String zip_code;

    private NamedEntity city;
    private IdentifiedEntity state;
    private IdentifiedEntity country;

    private SearchLocation search_location;

    private Double latitude;
    private Double longitude;
    private Long id;

}




