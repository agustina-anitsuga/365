package com.anitsuga.shop.lambda;

import com.anitsuga.shop.api.nube.NubeRestClient;
import com.anitsuga.shop.api.nube.model.Product;

public class ProductEraser implements NubeLambdaFunction {

    private NubeRestClient client = new NubeRestClient();

    @Override
    public Product apply(Product product) {
        client.deleteProduct(product.getId());
        return product;
    }
}
