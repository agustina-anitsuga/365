package com.anitsuga.invoicer.api;

import java.util.List;

/**
 * Order
 */
public class Order {

    private String id;
    private List<OrderItem> order_items;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<OrderItem> getOrder_items() {
        return order_items;
    }

    public void setOrder_items(List<OrderItem> order_items) {
        this.order_items = order_items;
    }
}
