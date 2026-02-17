package com.anitsuga.shop.lambda;

import com.anitsuga.shop.api.nube.NubeRestClient;
import com.anitsuga.shop.api.nube.model.Product;
import com.anitsuga.shop.api.nube.model.WritableProduct;

import java.util.List;

public abstract class CategoryUpdater implements NubeLambdaFunction {

    private List<Long> categories ;

    /**
     * Client to Nube
     */
    private final NubeRestClient nubeClient = new NubeRestClient();

    public CategoryUpdater(){
        categories = getCategories();
    }

    public abstract List<Long> getCategories();

    @Override
    public Product apply(Product product) {
        WritableProduct edit = new WritableProduct();
        edit.setId(product.getId());
        edit.setCategories(categories);
        return nubeClient.updateProduct(edit);
    }

}
