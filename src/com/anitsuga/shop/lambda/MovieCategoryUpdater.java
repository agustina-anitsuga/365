package com.anitsuga.shop.lambda;

import java.util.List;

public class MovieCategoryUpdater extends CategoryUpdater {

    public List<Long> getCategories(){
        // TODO extract to property
        List<Long> movie4kcategories = List.of(
                Long.valueOf(36910106),
                Long.valueOf(36864168),
                Long.valueOf(36864167),
                Long.valueOf(36864169)
        );
        return movie4kcategories;
    }

}
