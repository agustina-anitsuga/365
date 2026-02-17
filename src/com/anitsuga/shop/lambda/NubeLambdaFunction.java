package com.anitsuga.shop.lambda;

import com.anitsuga.shop.api.nube.model.Product;

public interface NubeLambdaFunction {

    Product apply(Product product );

}
