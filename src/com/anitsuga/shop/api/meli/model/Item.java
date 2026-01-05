package com.anitsuga.shop.api.meli.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class Item {

    private String id;
    private String site_id;
    private String title;
    private String family_name;
    private Long seller_id;
    private String category_id;
    private String user_product_id;
    private Long official_store_id;

    private BigDecimal price;
    private BigDecimal base_price;
    private BigDecimal original_price;

    private String inventory_id;
    private String currency_id;

    private Integer initial_quantity;
    private Integer available_quantity;
    private Integer sold_quantity;

    private List<SaleTerm> sale_terms;

    private String buying_mode;
    private String listing_type_id;

    //private OffsetDateTime startTime;
    //private OffsetDateTime stopTime;
    //private OffsetDateTime endTime;
    //private OffsetDateTime expirationTime;

    private String condition;
    private String permalink;

    private String thumbnail_id;
    private String thumbnail;

    private List<Picture> pictures;
    private String video_id;

    private List<Object> descriptions;

    private Boolean accepts_mercadopago;
    private List<Object> non_mercado_pago_payment_methods;

    private Shipping shipping;
    private String international_delivery_mode;

    private SellerAddress seller_address;
    private SellerContact seller_contact;

    private Map<String, Object> location;

    private GeoLocation geolocation;
    private List<Object> coverage_areas;

    private List<Attribute> attributes;
    private List<Object> warnings;

    private String listing_source;
    private List<Object> variations;

    private String status;
    private List<Object> sub_status;

    private List<String> tags;

    private String warranty;
    private String catalog_product_id;
    private String domain_id;

    private String seller_custom_field;
    private String parent_item_id;
    private Object differential_pricing;

    private List<String> deal_ids;
    private Boolean automatic_relist;

    //private OffsetDateTime dateCreated;
    //private OffsetDateTime lastUpdated;

    private BigDecimal health;
    private Boolean catalog_listing;

    private List<Object> item_relations;
    private List<String> channels;

}
