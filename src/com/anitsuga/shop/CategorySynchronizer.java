package com.anitsuga.shop;

import com.anitsuga.shop.api.meli.MeliRestClient;
import com.anitsuga.shop.api.meli.model.Attribute;
import com.anitsuga.shop.api.meli.model.CategoryPath;
import com.anitsuga.shop.api.nube.LanguageConfig;
import com.anitsuga.shop.api.nube.NubeRestClient;
import com.anitsuga.shop.api.nube.model.Category;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class CategorySynchronizer {

    public static final String CATEGORY_SEPARATOR = " // ";

    private final NubeRestClient nubeClient = new NubeRestClient();

    private final MeliRestClient meliClient = new MeliRestClient();

    private final Map<String, List<Category>> categories = new HashMap<>();

    private final List<Category> allNubeCategories = new ArrayList<>();

    private Map<String,String> categoryMappings = new HashMap<>();


    public CategorySynchronizer(){
        loadCategoryMappings();
        List<Category> nubeCats = nubeClient.getCategories();
        allNubeCategories.addAll(nubeCats);
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

        String categoryKey = sb.toString();
        return categoryKey;
    }

    private void loadCategoryMappings() {
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, String>>() {}.getType();
        InputStream inputStream = CategorySynchronizer.class
                .getClassLoader()
                .getResourceAsStream("category-mappings.json");
        if (inputStream == null) {
            throw new RuntimeException("File category-mappings.json not found in resources");
        }
        try (InputStreamReader reader =
                     new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            categoryMappings = gson.fromJson(reader, mapType);
        } catch (IOException e) {
            throw new RuntimeException("Category Mappings cannot be read.");
        }
    }

    private String getCategoryMapping(String categoryKey) {
        return categoryMappings.get(categoryKey);
    }

    private boolean hasCategoryMapping(String categoryKey) {
        return categoryMappings.containsKey(categoryKey);
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

        List<CategoryPath> path = category.getPath_from_root();
        CategoryPath rootCategory = null;
        if(path != null && path.size() > 0 ) {
            rootCategory = path.get(0);
            if (this.rootCategoryIsMapped(rootCategory)){
                if (!categories.containsKey(getCategoryKey(rootCategory))) {
                    this.createRootNubeCategory(rootCategory);
                }
            } else if(!categories.containsKey(getCategoryKey(category))){
                createNubeCategoryPath(category);
            }
        }

        if( (leafCategory!=null) && !categories.containsKey(getLeafCategoryKey(category,leafCategory))){
            createNubeLeafCategoryPath(category,leafCategory);
        }

        List<Long> ret = null;
        if( rootCategory!=null && this.rootCategoryIsMapped(rootCategory) ){
            ret = categories.get(getCategoryKey(rootCategory)).stream().map(Category::getId).toList();
        } else if ( leafCategory == null ) {
            ret = categories.get(getCategoryKey(category)).stream().map(Category::getId).toList();
        } else {
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
        String categoryKey = null;

        if( categoryPath!=null && categoryPath.size()>0 ) {
            CategoryPath rootCategory = categoryPath.get(0);
            if(this.rootCategoryIsMapped(rootCategory)) {
                categoryKey = this.getCategoryMapping(rootCategory.getName()) + CATEGORY_SEPARATOR;
            } else {
                Collections.reverse(categoryPath);
                categoryKey = categoryPath.stream().map(CategoryPath::getName)
                        .collect(Collectors.joining(CATEGORY_SEPARATOR)) + CATEGORY_SEPARATOR;
            }
        }

        return categoryKey;
    }

    private String getRootCategoryKey(CategoryPath rootCategory) {
        return rootCategory.getName() + CATEGORY_SEPARATOR;
    }

    private String getCategoryKey(CategoryPath categoryPath) {
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
        if(!category.getPath_from_root().isEmpty()){
            List<CategoryPath> categoryPaths = category.getPath_from_root();
            Category cat = null;

            CategoryPath rootCategory = categoryPaths.get(0);
            if( rootCategoryIsMapped(rootCategory) ){
                cat = createRootNubeCategory(rootCategory);
                allNubeCategories.add(cat);
                categories.put(getCategoryKey(cat), getCategoryPath(cat, allNubeCategories));
            }
            else {
                for (CategoryPath categoryPath : categoryPaths) {
                    if (!categories.containsKey(getCategoryKey(categoryPath))) {
                        cat = createNubeCategory(categoryPath, cat);
                        allNubeCategories.add(cat);
                        categories.put(getCategoryKey(cat), getCategoryPath(cat, allNubeCategories));
                    } else {
                        cat = categories.get(getCategoryKey(categoryPath)).get(0);
                    }
                }
            }
        }
    }

    private boolean rootCategoryIsMapped(CategoryPath rootCategory) {
        return this.hasCategoryMapping(rootCategory.getName());
    }

    private Category createRootNubeCategory(CategoryPath categoryPath) {

        Category nubeCategory = new Category();

        Map<String,String> name = new HashMap<>();
        name.put(LanguageConfig.getDefaultLanguage(),getCategoryMapping(categoryPath.getName()));
        nubeCategory.setName(name);

        nubeCategory = nubeClient.createCategory(nubeCategory);
        allNubeCategories.add(nubeCategory);
        categories.put(getCategoryKey(nubeCategory), getCategoryPath(nubeCategory, allNubeCategories));

        return nubeCategory;
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
