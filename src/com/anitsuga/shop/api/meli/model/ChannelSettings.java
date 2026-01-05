package com.anitsuga.shop.api.meli.model;

import lombok.Data;

import java.util.Map;

@Data
public class ChannelSettings {

    private String channel;
    private Map<String, Object> settings;

}

