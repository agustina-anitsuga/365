package com.anitsuga.shop.api.meli;


import com.anitsuga.fwk.utils.AppProperties;
import com.anitsuga.fwk.utils.RestClient;
import com.anitsuga.shop.api.meli.model.Category;
import com.anitsuga.shop.api.meli.model.CategoryAttribute;
import com.anitsuga.shop.api.meli.model.Item;
import com.anitsuga.shop.api.meli.model.ItemDescription;
import com.anitsuga.shop.api.meli.model.Page;
import com.anitsuga.shop.model.Listing;
import com.google.gson.Gson;

import java.util.ArrayList;
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

    public List<Listing> getAllPublicationIds(String userId){
        String offset = null;
        List<Listing> result = new ArrayList<>();
        do{
            //System.out.println("result.size="+result.size());
            Page page = getPageFrom(offset,userId);
            for (String id: page.getResults()) {
                Listing listing = new Listing();
                listing.setMeliId(id);
                result.add(listing);
            }
            offset = page.getScroll_id();
            //System.out.println("Offset="+offset);
        } while ( offset!=null && !offset.trim().equals("") );
        return result;
    }

    private Page getPageFrom(String offset, String userId){
        Page ret = null;
        String url = String.format(
                offset==null? "%s/users/%s/items/search?search_type=scan&limit=100" : "%s/users/%s/items/search?search_type=scan&limit=100&scroll_id=%s",
                API_MERCADOLIBRE_COM,
                userId,
                offset
        );
        String response = restClient.get(url, buildHeader() );
        if (response != null) {
            ret = new Gson().fromJson(response,Page.class);
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
