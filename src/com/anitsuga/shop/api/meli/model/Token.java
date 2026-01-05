package com.anitsuga.shop.api.meli.model;

import lombok.Data;

@Data
public class Token {

    private String access_token;
    private String token_type;
    private long expires_in;
    private String scope;
    private long user_id;
    private String refresh_token;

}
