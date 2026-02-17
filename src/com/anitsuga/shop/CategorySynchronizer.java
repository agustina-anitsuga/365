package com.anitsuga.shop;

import com.anitsuga.shop.api.meli.MeliRestClient;
import com.anitsuga.shop.api.meli.model.Attribute;
import com.anitsuga.shop.api.meli.model.CategoryPath;
import com.anitsuga.shop.api.nube.LanguageConfig;
import com.anitsuga.shop.api.nube.NubeRestClient;
import com.anitsuga.shop.api.nube.model.Category;

import java.util.*;
import java.util.stream.Collectors;

public class CategorySynchronizer {

    public static final String CATEGORY_SEPARATOR = " // ";

    private final NubeRestClient nubeClient = new NubeRestClient();

    private final MeliRestClient meliClient = new MeliRestClient();

    private final Map<String, List<Category>> categories = new HashMap<>();

    private final List<Category> allNubeCategories = new ArrayList<>();



    public CategorySynchronizer(){
        List<Category> nubeCats = nubeClient.getCategories();
        for (Category category: nubeCats) {
            allNubeCategories.add(category);
        }
        for (Category category: nubeCats) {
            String categoryKey = getCategoryKey(category);
            categories.put( categoryKey, getCategoryPath(category,nubeCats) );
        }
    }

    private Category getCategoryById(Long id){
        return (id!=null && id!=0)? allNubeCategories.stream().filter(c -> id.equals(c.getId()) ).findFirst().get() : null;
    }

    private String getCategoryKey(Category category) {
        StringBuffer sb = new StringBuffer();
        Category cat = category;
        while( cat!=null ) {
            sb.append(cat.getName().get(LanguageConfig.getDefaultLanguage()));
            sb.append(CATEGORY_SEPARATOR);
            cat = getCategoryById(cat.getParent());
        }
        return sb.toString();
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

    public List<Long> mapToCategories( String categoryId, Attribute leafCategory ){

        com.anitsuga.shop.api.meli.model.Category category = meliClient.getCategory(categoryId);

        if(!categories.containsKey(getCategoryKey(category))){
            createNubeCategoryPath(category);
        }

        if( (leafCategory!=null) && !categories.containsKey(getLeafCategoryKey(category,leafCategory))){
            createNubeLeafCategoryPath(category,leafCategory);
        }

        List<Long> ret = categories.get(getCategoryKey(category)).stream().map(Category::getId).toList();
        if( leafCategory!=null ){
            ret = categories.get(getLeafCategoryKey(category,leafCategory)).stream().map(Category::getId).toList();
        }

        return ret;
    }

    private String getLeafCategoryKey(com.anitsuga.shop.api.meli.model.Category category, Attribute leafCategory) {
        String ret = leafCategory.getValue_name() + CATEGORY_SEPARATOR  + getCategoryKey(category) ;
        return ret;
    }

    private String getCategoryKey(com.anitsuga.shop.api.meli.model.Category category) {
        List<CategoryPath> categoryPath = new ArrayList<>(category.getPath_from_root());
        Collections.reverse(categoryPath);
        String ret = categoryPath.stream().map(CategoryPath::getName)
                .collect(Collectors.joining(CATEGORY_SEPARATOR)) + CATEGORY_SEPARATOR;
        return ret;
    }

    private String getCategoryKey(CategoryPath categoryPath) { // TODO
        String meliCatId = categoryPath.getId();
        com.anitsuga.shop.api.meli.model.Category category = meliClient.getCategory(meliCatId);
        return getCategoryKey(category);
    }

    private void createNubeLeafCategoryPath(com.anitsuga.shop.api.meli.model.Category category, Attribute leafCategory) {
        List<Category> nubeCategories = new ArrayList<>(categories.get(getCategoryKey(category)));

        Category cat = createNubeCategory(category,leafCategory);
        nubeCategories.add(0,cat);

        categories.put(getLeafCategoryKey(category,leafCategory),nubeCategories);
        allNubeCategories.add(cat);
    }

    private Category createNubeCategory( com.anitsuga.shop.api.meli.model.Category parent, Attribute leafCategory ) {

        Category nubeCategory = new Category();

        Map<String,String> name = new HashMap<>();
        name.put(LanguageConfig.getDefaultLanguage(),leafCategory.getValue_name());
        nubeCategory.setName(name);

        if(parent!=null){
            List<Category> categoryPath = categories.get(getCategoryKey(parent));
            Category parentCategory = categoryPath.get(0);
            Long parentId = parentCategory.getId();
            nubeCategory.setParent(parentId);
        }

        nubeCategory = nubeClient.createCategory(nubeCategory);

        return nubeCategory;
    }

    private void createNubeCategoryPath(com.anitsuga.shop.api.meli.model.Category category) {
        List<Category> nubeCategories = new ArrayList<>();

        if(!category.getPath_from_root().isEmpty()){
            List<CategoryPath> categoryPaths = category.getPath_from_root();
            Category cat = null;
            for (CategoryPath categoryPath: categoryPaths ) {
                if(!categories.containsKey(getCategoryKey(categoryPath))) {
                    cat = createNubeCategory(categoryPath, cat);
                    allNubeCategories.add(cat);
                    categories.put(getCategoryKey(cat),getCategoryPath(cat,allNubeCategories));
                } else {
                    cat = categories.get(getCategoryKey(categoryPath)).get(0);
                }
            }
        }
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
