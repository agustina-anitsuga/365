package com.anitsuga.shop;

import com.anitsuga.fwk.utils.AppProperties;
import com.anitsuga.shop.api.meli.MeliRestClient;
import com.anitsuga.shop.api.meli.model.Item;
import com.anitsuga.shop.api.meli.model.ItemDescription;
import com.anitsuga.shop.api.meli.model.Picture;
import com.anitsuga.shop.api.nube.LanguageConfig;
import com.anitsuga.shop.api.nube.NubeRestClient;
import com.anitsuga.shop.api.nube.model.*;
import com.anitsuga.shop.model.Listing;
import com.anitsuga.shop.reader.InputDataReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ShopSynchronizer {

    /**
     * PRODUCTS_TO_SYNCH_FILE
     */
    protected static final String PRODUCTS_TO_SYNCH_FILE = "products.to.synch.file";

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ShopSynchronizer.class.getName());

    /**
     * Nube Category Service
     */
    CategorySynchronizer categorySynchronizer = null;

    /**
     * Client to Meli
     */
    MeliRestClient meliClient = null;

    /**
     * Client to Nube
     */
    NubeRestClient nubeClient = null;


    public ShopSynchronizer(){
        categorySynchronizer = new CategorySynchronizer();
        meliClient = new MeliRestClient();
        nubeClient = new NubeRestClient();
    }

    /**
     * main
     * @param args
     */
    public static void main(String[] args) {
        ShopSynchronizer self = new ShopSynchronizer();
        self.run();
    }

    /**
     * run
     */
    public void run(){

        List<Listing> listings = getListingsToSynch();

        int count = 0;
        int total = listings.size();
        for (Listing listing : listings) {
            LOGGER.info("Synchronizing list item "+listing.getId()+ " ["+(++count)+"/"+total+"]");
            String result = synch(listing);
            listing.setResult(result);
            LOGGER.info(result);
            LOGGER.info("---");
        }

        writeResults(listings);
    }

    /**
     * writeResults TODO
     */
    private void writeResults(List<Listing> listings) {
        /*
        String localPath = FileUtils.getLocalPath();
        String filename = localPath + outputFilePrefix();
        ResultExcelWriter writer = new ResultExcelWriter();
        writer.write(filename, sales);
        System.out.println("Finished invoicing job");
        */
    }

    /**
     * outputFilePrefix
     * @return
     */
    private String outputFilePrefix() {
        return "synchronized-products";
    }

    /**
     * synch
     * @param listing
     * @return
     */
    private String synch(Listing listing) {
        String ret = "";
        try {

            // Get data from MercadoLibre
            Item item = meliClient.getItemById(listing.getId());

            // Look up listing in tienda nube
            String sku = item.getId();
            BaseProduct product = nubeClient.getProductBySKU(sku);

            BaseProduct result = null;
            if( product!=null ){
                // If listing exists, update price and stock
                product = patchProductFromListing(item, (Product) product);
                result = nubeClient.patchProductStockPrice( (Product) product);
            } else {
                // If listing does not exist, create it
                ItemDescription description = meliClient.getItemDescriptionById(item.getId());
                product = createProductFromListing(item,description,item.getCategory_id());
                result = nubeClient.createProduct( (NewProduct) product);
            }

            // verify creation/update - TODO
            if(result!=null) {
                ret = String.valueOf(result.getId());
            } else {
                ret = "Error";
            }

        } catch (Exception e) {
            ret = e.getMessage();
        }
        return ret;
    }

    private Product patchProductFromListing(Item item, Product product) {
        Product ret = new Product();
        ret.setId(product.getId());

        Long variantId = product.getVariants().get(0).getId();
        Variant variant = new Variant();
        variant.setId(variantId);
        variant.setPrice(item.getPrice().toString());

        InventoryLevel il = new InventoryLevel();
        il.setStock(item.getAvailable_quantity());
        variant.setInventory_levels(List.of(il));

        ret.setVariants(List.of(variant));

        return ret;
    }

    private NewProduct createProductFromListing(Item item, ItemDescription description, String categoryId) {
        NewProduct ret = new NewProduct();
        String language = LanguageConfig.getDefaultLanguage();

        Map<String, String> title = new HashMap();
        title.put(language,item.getTitle());
        ret.setName(title);

        Map<String, String> desc = new HashMap();
        desc.put(language,description.getPlain_text());
        ret.setDescription(desc);

        List<Image> images = item.getPictures().stream()
                .map(this::extractImage)
                .filter(Objects::nonNull)
                .toList();
        ret.setImages(images);

        Variant variant = new Variant();
        variant.setPrice(item.getPrice().toString());
        variant.setSku(item.getId());
        variant.setStock(item.getAvailable_quantity());
        variant.setStock_management(true);
        variant.setVisible(true);
        ret.setVariants(List.of(variant));

        List<Long> categories = categorySynchronizer.mapToCategories(categoryId);
        ret.setCategories(categories);

        ret.setRequires_shipping(true);
        ret.setPublished(true);

        return ret;
    }

    private Image extractImage(Picture picture) {
        Image image = new Image();
        image.setSrc(picture.getSecure_url());
        return image;
    }

    /**
     * getProductsToSynch
     * @return
     */
    private List<Listing> getListingsToSynch() {
        String filename = AppProperties.getInstance().getProperty(PRODUCTS_TO_SYNCH_FILE);
        InputDataReader reader = new InputDataReader();
        return reader.read(filename);
    }

}
