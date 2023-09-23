package com.anitsuga.invoicer.api;

import java.util.List;

public class Pack {

    private List<Order> orders;

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
