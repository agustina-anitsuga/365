package com.anitsuga.shop;

import com.anitsuga.shop.api.meli.MeliRestClient;
import com.anitsuga.shop.api.meli.model.CategoryPath;
import com.anitsuga.shop.api.nube.LanguageConfig;
import com.anitsuga.shop.api.nube.NubeRestClient;
import com.anitsuga.shop.api.nube.model.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategorySynchronizer {

    private final NubeRestClient nubeClient = new NubeRestClient();

    private final MeliRestClient meliClient = new MeliRestClient();

    private final Map<String, List<Category>> categories = new HashMap<>();

    public CategorySynchronizer(){
        List<Category> nubeCats = nubeClient.getCategories();
        for (Category category: nubeCats) {
            String categoryName = category.getName().get(LanguageConfig.getDefaultLanguage());
            categories.put( categoryName, getCategoryPath(category,nubeCats) );
        }
    }

    private List<Category> getCategoryPath(Category category, List<Category> allCategories) {
        List<Category> ret = new ArrayList<>();
        Category cat = category;
        while( cat!=null ){
            ret.add(cat);
            Long parent = cat.getParent();
            if( parent!=0 ) {
                cat = allCategories.stream().filter(c -> c.getId().equals(parent)).findFirst().get();
            } else {
                cat = null;
            }
        }
        return ret;
    }

    public List<Long> mapToCategories( String categoryId ){

        com.anitsuga.shop.api.meli.model.Category category = meliClient.getCategory(categoryId);

        if(!categories.containsKey(category.getName())){
            createNubeCategoryPath(category);
        }

        return categories.get(category.getName()).stream().map(Category::getId).toList();
    }

    private void createNubeCategoryPath(com.anitsuga.shop.api.meli.model.Category category) {
        List<Category> nubeCategories = new ArrayList<>();

        if(!category.getPath_from_root().isEmpty()){
            List<CategoryPath> categoryPaths = category.getPath_from_root();
            Category cat = null;
            for (CategoryPath categoryPath: categoryPaths ) {
                cat = createNubeCategory(categoryPath,cat);
                nubeCategories.add(cat);
            }
        }

        categories.put(category.getName(),nubeCategories);
    }

    private Category createNubeCategory( CategoryPath categoryPath, Category parent ) {

        Category nubeCategory = new Category();

        Map<String,String> name = new HashMap<>();
        name.put(LanguageConfig.getDefaultLanguage(),categoryPath.getName());
        nubeCategory.setName(name);

        if(parent!=null){
            nubeCategory.setParent(parent.getId());
        }

        nubeCategory = nubeClient.createCategory(nubeCategory);

        return nubeCategory;
    }
}
