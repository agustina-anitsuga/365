package com.anitsuga.invoicer.api;

import com.anitsuga.fwk.utils.AppProperties;
import com.anitsuga.fwk.utils.RestClient;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MeliRestClient
 */
public class MeliRestClient {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MeliRestClient.class.getName());

    private static final RestClient restClient = new RestClient();

    /**
     * getOrderDetails
     * @param orderId
     * @return
     */
    public List<OrderItem> getOrderDetails(String orderId ){
        String response = restClient.get("https://api.mercadolibre.com/orders/"+orderId.trim(), buildHeader());

        List<OrderItem> ret = null;
        if( response!=null ) {
            Order order = new Gson().fromJson(response, Order.class);
            if (orderId.equals(order.getId()))
                ret = order.getOrder_items();
        }
        return ret;
    }

    /**
     * getNotes
     * @param orderId
     * @return
     */
    public List<Note> getNotes(String orderId ){
        String response = restClient.get("https://api.mercadolibre.com/orders/"+orderId.trim()+"/notes", buildHeader());

        List<Note> ret = null;
        if( response!=null ) {
            response = "{" + response.substring(2, response.length() - 2) + " }";
            Notes notes = new Gson().fromJson(response, Notes.class);
            if (orderId.equals(notes.getOrder_id()))
                ret = notes.getResults();
        }
        return ret;
    }

    /**
     * getBillingInfo
     * @param orderId
     * @return
     */
    public BillingInfo getBillingInfo(String orderId ){
        String response = restClient.get("https://api.mercadolibre.com/orders/"+orderId.trim()+"/billing_info", buildHeader());

        BillingInfo ret = null;
        if( response!=null ) {
            OrderBillingInfo bi = new Gson().fromJson(response, OrderBillingInfo.class);
            ret = bi.getBilling_info();
        }
        return ret;
    }

    /**
     * buildHeader
     * @return
     */
    private Map<String,String> buildHeader () {
        Map<String,String> header = new HashMap<String,String>();
        header.put("Content-Type", "application/json; charset=UTF-8");
        header.put("Authorization", "Bearer "+ AppProperties.getInstance().getProperty("api.token"));
        return header;
    }
}
