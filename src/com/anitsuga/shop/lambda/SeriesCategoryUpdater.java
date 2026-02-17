package com.anitsuga.shop.lambda;

import java.util.List;

public class SeriesCategoryUpdater extends CategoryUpdater {

    public List<Long> getCategories(){
        // TODO extract to property
        List<Long> series4kcategories = List.of(
                Long.valueOf(36864167),
                Long.valueOf(36912489),
                Long.valueOf(36912490),
                Long.valueOf(36912605)
        );
        return series4kcategories;
    }

}
