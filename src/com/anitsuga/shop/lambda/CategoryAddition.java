package com.anitsuga.shop.lambda;

import com.anitsuga.shop.api.nube.NubeRestClient;
import com.anitsuga.shop.api.nube.model.Product;
import com.anitsuga.shop.api.nube.model.WritableProduct;

import java.util.List;

public class CategoryAddition implements NubeLambdaFunction {

    private static List<Long> categories = List.of(
                    Long.valueOf(36910106),
                    Long.valueOf(36864168),
                    Long.valueOf(36864167),
                    Long.valueOf(36864169)
                    );

    /**
     * Client to Nube
     */
    private final NubeRestClient nubeClient = new NubeRestClient();

    @Override
    public Product apply(Product product) {
        WritableProduct edit = new WritableProduct();
        edit.setId(product.getId());
        edit.setCategories(categories);
        return nubeClient.updateProduct(edit);
    }

}
