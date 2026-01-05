package com.anitsuga.shop.api.meli;

import com.anitsuga.fwk.utils.AppProperties;
import com.anitsuga.fwk.utils.RestClient;
import com.anitsuga.shop.api.meli.model.Category;
import com.anitsuga.shop.api.meli.model.CategoryAttribute;
import com.anitsuga.shop.api.meli.model.Item;
import com.anitsuga.shop.api.meli.model.ItemDescription;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MeliRestClient
 */
public class MeliRestClient {

    private static final RestClient restClient = new RestClient();

    public static final String API_MERCADOLIBRE_COM = "https://api.mercadolibre.com";


    /**
     * @param itemId
     */
    public ItemDescription getItemDescriptionById(String itemId ){
        ItemDescription ret = null;
        if(itemId!=null) {
            String url = String.format(
                    "%s/items/%s/description",
                    API_MERCADOLIBRE_COM,
                    itemId.trim()
            );
            String response = restClient.get(url, buildHeader() );
            if (response != null) {
                ret = new Gson().fromJson(response, ItemDescription.class);
            }
        }
        return ret;
    }

    public Item getItemById(String productId ){
        Item ret = null;
        String url = String.format(
                "%s/items/%s",
                API_MERCADOLIBRE_COM,
                productId
        );
        String response = restClient.get(url, buildHeader() );
        if (response != null) {
            ret = new Gson().fromJson(response, Item.class);
        }
        return ret;
    }

    public Category getCategory(String categoryId) {
        Category ret = null;
        String url = String.format(
                "%s/categories/%s",
                API_MERCADOLIBRE_COM,
                categoryId
        );
        String response = restClient.get(url, buildHeader() );
        if (response != null) {
            ret = new Gson().fromJson(response, Category.class);
        }
        return ret;
    }

    public List<CategoryAttribute> getCategoryAttributes(String categoryId) {
        List<CategoryAttribute> ret = null;
        String url = String.format(
                "%s/categories/%s/attributes",
                API_MERCADOLIBRE_COM,
                categoryId
        );
        String response = restClient.get(url, buildHeader() );
        if (response != null) {
            ret = new Gson().fromJson(response,List.class);
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
