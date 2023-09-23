package com.anitsuga.invoicer.api;

import java.util.List;

/**
 * Order
 */
public class Order {

    private String id;

    private boolean fulfilled;

    private String status;

    private List<String> tags;

    private String cancel_details;

    private String pack_id;

    private OrderRequest order_request;

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

    public boolean isFulfilled() {
        return fulfilled;
    }

    public void setFulfilled(boolean fulfilled) {
        this.fulfilled = fulfilled;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCancel_details() {
        return cancel_details;
    }

    public void setCancel_details(String cancel_details) {
        this.cancel_details = cancel_details;
    }

    public OrderRequest getOrder_request() {
        return order_request;
    }

    public void setOrder_request(OrderRequest order_request) {
        this.order_request = order_request;
    }

    public String getPack_id() {
        return pack_id;
    }

    public void setPack_id(String pack_id) {
        this.pack_id = pack_id;
    }
}
