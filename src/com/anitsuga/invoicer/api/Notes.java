package com.anitsuga.invoicer.api;

import java.util.List;

public class Notes {

    private List<Note> results;

    private String order_id;

    public List<Note> getResults() {
        return results;
    }

    public void setResults(List<Note> results) {
        this.results = results;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
}
