package com.anitsuga.shop.api.meli.model;

import lombok.Data;


@Data
public class ItemDescription {

    private String text;
    private String plain_text;
    //private OffsetDateTime lastUpdated;
    //private OffsetDateTime dateCreated;
    private Snapshot snapshot;

    @Data
    public static class Snapshot {

        private String url;
        private int width;
        private int height;
        private String status;

    }
}

