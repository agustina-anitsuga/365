package com.anitsuga.shop.api.meli.model;

import lombok.Data;

import java.util.List;

@Data
public class Category {

    private String id;
    private String name;
    private String picture;
    private String permalink;

    private Long total_items_in_this_category;

    private List<CategoryPath> path_from_root;
    private List<Category> children_categories;

    private String attribute_types;
    private CategorySettings settings;

    private List<ChannelSettings> channels_settings;

    private String meta_categ_id;
    private Boolean attributable;

    //private OffsetDateTime dateCreated;

}
