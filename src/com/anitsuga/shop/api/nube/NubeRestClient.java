package com.anitsuga.shop.api.nube;

import com.anitsuga.fwk.utils.AppProperties;
import com.anitsuga.fwk.utils.RestClient;
import com.anitsuga.shop.api.nube.model.Category;
import com.anitsuga.shop.api.nube.model.NewProduct;
import com.anitsuga.shop.api.nube.model.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * NubeRestClient
 * https://dev.tiendanube.com/docs/applications/overview#autenticando-seu-aplicativo
 * https://dev.tiendanube.com/docs/developer-tools/nuvemshop-api
 */
public class NubeRestClient {

    private static final String API_TIENDA_NUBE = AppProperties.getInstance().getProperty("api.tiendanube.url");

    private static final RestClient restClient = new RestClient();


    public Product getProductById(String productId ){
        Product ret = null;
        String url = String.format(
                "%s/products/%s",
                API_TIENDA_NUBE,
                productId
        );
        String response = restClient.get(url, buildHeader() );
        if (response != null) {
            ret = new Gson().fromJson(response, Product.class);
        }
        return ret;
    }

    public Product getProductBySKU(String productSKU ){
        Product ret = null;
        String url = String.format(
                "%s/products/sku/%s",
                API_TIENDA_NUBE,
                productSKU
        );
        String response = restClient.get(url, buildHeader() );
        if (response != null) {
            ret = new Gson().fromJson(response, Product.class);
        }
        return ret;
    }

    public Product createProduct( NewProduct product) {
        Product ret = null;
        String url = String.format(
                "%s/products/",
                API_TIENDA_NUBE
        );
        String body = new Gson().toJson(product);
        String response = restClient.postJson(url, buildHeader(), body);
        if (response != null) {
            ret = new Gson().fromJson(response, Product.class);
        }
        return ret;
    }

    public Product patchProductStockPrice(Product product) {
        Product ret = null;
        String url = String.format(
                "%s/products/stock-price",
                API_TIENDA_NUBE
        );
        String body = new Gson().toJson(List.of(product));
        String response = restClient.patchJson(url, buildHeader(), body);
        if (response != null) {
            Type productListType = new TypeToken<List<Product>>() {}.getType();
            List<Product> responseObject = new Gson().fromJson(response, productListType);
            ret = responseObject.get(0);
        }
        return ret;
    }

    public List<Category> getCategories(){
        List<Category> ret = null;
        String url = String.format(
                "%s/categories",
                API_TIENDA_NUBE
        );
        String response = restClient.get(url, buildHeader());
        if (response != null) {
            Type categoryListType = new TypeToken<List<Category>>() {}.getType();
            ret = new Gson().fromJson(response, categoryListType);
        }
        return ret;
    }

    public Category createCategory( Category category ){
        Category ret = null;
        String url = String.format(
                "%s/categories",
                API_TIENDA_NUBE
        );
        String body = new Gson().toJson(category);
        String response = restClient.postJson(url, buildHeader(), body);
        if (response != null) {
            ret = new Gson().fromJson(response, Category.class);
        }
        return ret;
    }

    private Map<String, String> buildHeader() {
        Map<String,String> header = new HashMap<String,String>();
        header.put("User-Agent", AppProperties.getInstance().getProperty("api.tiendanube.user-agent") );
        header.put("Content-Type", "application/json; charset=UTF-8");
        header.put("Authentication", "bearer "+ AppProperties.getInstance().getProperty("api.tiendanube.token"));
        return header;
    }

}
