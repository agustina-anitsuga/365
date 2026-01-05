package com.anitsuga.shop.api.meli.model;

import lombok.Data;

import java.util.List;

@Data
public class CategorySettings {

    private Boolean adult_content;
    private Boolean buying_allowed;
    private List<String> buying_modes;

    private String catalog_domain;
    private String coverage_areas;
    private List<String> currencies;

    private Boolean fragile;
    private String immediate_payment;

    private List<String> item_conditions;
    private Boolean items_reviews_allowed;

    private Boolean listing_allowed;

    private Integer max_description_length;
    private Integer max_pictures_per_item;
    private Integer max_pictures_per_item_var;
    private Integer max_sub_title_length;
    private Integer max_title_length;
    private Integer max_variations_allowed;

    private Double maximum_price;
    private String maximum_price_currency;

    private Double minimum_price;
    private String minimum_price_currency;

    private String mirror_category;
    private String mirror_master_category;
    private List<String> mirror_slave_categories;

    private String price;
    private String reservation_allowed;

    private List<String> restrictions;

    private Boolean rounded_address;
    private String seller_contact;

    private List<String> shipping_options;
    private String shipping_profile;

    private String show_contact_information;
    private String simple_shipping;
    private String stock;

    private String sub_vertical;
    private Boolean subscribable;

    private List<String> tags;

    private String vertical;
    private String vip_subdomain;

    private List<String> buyer_protection_programs;
    private String status;

}

