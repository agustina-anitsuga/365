package com.anitsuga.shop;

import com.anitsuga.fwk.utils.AppProperties;
import com.anitsuga.fwk.utils.FileUtils;
import com.anitsuga.shop.api.meli.MeliRestClient;
import com.anitsuga.shop.api.meli.model.*;
import com.anitsuga.shop.api.nube.LanguageConfig;
import com.anitsuga.shop.api.nube.NubeRestClient;
import com.anitsuga.shop.api.nube.model.*;
import com.anitsuga.shop.model.Listing;
import com.anitsuga.shop.reader.InputDataReader;
import com.anitsuga.shop.writer.ResultExcelWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
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
     * Active status in Meli
     */
    private static final String ACTIVE = "active";

    /**
     * Nube Category Service
     */
    private final CategorySynchronizer categorySynchronizer;

    /**
     * Client to Meli
     */
    private final MeliRestClient meliClient;

    /**
     * Client to Nube
     */
    private final NubeRestClient nubeClient;


    public ShopSynchronizer(){
        categorySynchronizer = new CategorySynchronizer();
        meliClient = new MeliRestClient();
        nubeClient = new NubeRestClient();
    }

    /**
     * main
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
            Instant start = Instant.now();
            String result = processListing(listing);
            Duration duration = Duration.between(start, Instant.now());
            listing.setResult(result);
            listing.setDuration(duration.toMillis());
            LOGGER.info(result);
            LOGGER.info("Duration:"+duration.toMillis());
            LOGGER.info("---");
        }

        writeResults(listings);
    }

    /**
     * writeResults TODO
     */
    private void writeResults(List<Listing> listings) {
        String localPath = FileUtils.getLocalPath();
        String filename = localPath + outputFilePrefix();
        ResultExcelWriter writer = new ResultExcelWriter();
        writer.write(filename, listings);
        System.out.println("Finished invoicing job");
    }

    /**
     * outputFilePrefix
     */
    private String outputFilePrefix() {
        return "synchronized-products";
    }

    /**
     * processListing
     */
    private String processListing(Listing listing) {
        String ret = "";
        try {

            // Get data from MercadoLibre
            Item item = meliClient.getItemById(listing.getId());

            // is publication was not found, throw an error
            if( item==null ){
                throw new Exception("Publication not found.");
            }

            // Look up listing in tienda nube
            Product product = nubeClient.getProductBySKU(item.getId());

            // Synchronize Products
            Product resultingProduct = synchronizeProduct(item, product);

            // verify synchronization - TODO improve
            if (resultingProduct != null) {
                ret = String.valueOf(resultingProduct.getId());
            } else {
                ret = "Error";
            }

        } catch (Exception e) {
            ret = e.getMessage();
        }
        return ret;
    }

    private Product synchronizeProduct( Item item, Product product ) throws Exception {

        Product resultingProduct = null;

        // if publication is not active in MELI OR it does not have immediate availability
        if( !productIsActive(item) || !stockIsImmediatelyAvailable(item) ){
            // but it is active in tienda nube
            if(productIsActive(product)) {
                // inactivate product in tienda nube
                resultingProduct = inactivateProduct(product);
            } else {
                // product is inactive in both systems, update can be ignored
                throw new Exception( "Product is inactive in both systems (or does not exist in tiendanube)" );
            }
        } else {
            // publication is active in MELI, so it should be created or update in tienda nueba
            resultingProduct = createOrUpdateProduct(item, product);
        }

        return resultingProduct;
    }

    private boolean productIsActive(Product product) {
        return (product != null) && product.getPublished();
    }

    private boolean productIsActive(Item item) {
        return ACTIVE.equals(item.getStatus());
    }

    private boolean stockIsImmediatelyAvailable(Item item) {
        boolean ret = true;
        Optional<SaleTerm> saleTerms = item.getSale_terms().stream().filter(s -> "MANUFACTURING_TIME".equals(s.getId()) ).findFirst();
        if( saleTerms.isPresent() ){
            SaleTerm saleTerm = saleTerms.get();
            ret = !(saleTerm.getValue_struct().getNumber().compareTo(BigDecimal.ZERO) > 0);
        }
        return ret;
    }

    private Product inactivateProduct(Product product) {
        return setProductStatus(product,false);
    }

    private Product reactivateProduct(Product product) {
        return setProductStatus(product,true);
    }

    private Product setProductStatus(Product product, boolean active ){
        WritableProduct prodToUpdate = new WritableProduct();
        prodToUpdate.setId(product.getId());
        prodToUpdate.setPublished(active);
        return nubeClient.updateProduct(prodToUpdate);
    }

    private Product createOrUpdateProduct(Item item, Product product) {
        Product result;
        if (product != null) {
            // If listing exists, update price and stock
            WritableProduct productToPatch = patchProductFromListing(item, (ReadableProduct) product);
            result = nubeClient.patchProductStockPrice(productToPatch);
            if( !product.getPublished() ) {
                // also need to reactivate product
                result = reactivateProduct(product); // TODO this can be enhanced to do a full update
            }
        } else {
            // If listing does not exist, create it
            ItemDescription description = meliClient.getItemDescriptionById(item.getId());
            product = createProductFromListing(item, description, item.getCategory_id());
            result = nubeClient.createProduct((WritableProduct) product);
        }
        return result;
    }

    private WritableProduct patchProductFromListing(Item item, ReadableProduct product) {

        Variant existingVariant = product.getVariants().get(0);

        WritableProduct ret = new WritableProduct();
        ret.setId(product.getId());

        Long variantId = existingVariant.getId();
        Variant variant = new Variant();
        variant.setId(variantId);
        variant.setPrice(item.getPrice().toString());

        InventoryLevel il = new InventoryLevel();
        il.setStock(item.getAvailable_quantity());
        variant.setInventory_levels(List.of(il));

        ret.setVariants(List.of(variant));

        return ret;
    }

    private WritableProduct createProductFromListing(Item item, ItemDescription description, String categoryId) {
        WritableProduct ret = new WritableProduct();
        String language = LanguageConfig.getDefaultLanguage();

        Map<String, String> title = new HashMap<>();
        title.put(language,item.getTitle());
        ret.setName(title);

        Map<String, String> desc = new HashMap<>();
        String nubeDesc = buildDescription(item,description);
        desc.put(language,nubeDesc);
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

        List<Long> categories = categorySynchronizer.mapToCategories(categoryId,getLeafCategoryAttribute(item));
        ret.setCategories(categories);

        ret.setRequires_shipping(true);
        ret.setPublished(true);

        return ret;
    }

    private Attribute getLeafCategoryAttribute(Item item) {
        Optional<Attribute> att = item.getAttributes().stream().filter(a -> a.getId().equals("MOVIE_FORMAT") ).findFirst();
        return att.isPresent() ? att.get() : null ;
    }

    private String buildDescription(Item item, ItemDescription description) {  // TODO use thymeleaf templates
        StringBuffer ret = new StringBuffer();

        if( description!=null ) {
            ret.append("<p class=\"text-md-left\">");
            ret.append(description.getPlain_text().replaceAll("\n", "<br/>"));
            ret.append("</p><p>&nbsp;</p>");
        }

        if(!item.getAttributes().isEmpty()) {
            ret.append("<p class=\"text-md-left\"><strong>Especificaciones t&eacute;cnicas</strong></p>");
            ret.append("<p class=\"text-md-left\">");
            List<String> excludedIds = getExcludedAttributeIds();
            List<Attribute> attributesToShow = item.getAttributes().stream().filter(a -> !excludedIds.contains(a.getId())).toList();
            for (Attribute attribute: attributesToShow) {
                ret.append("<ul class=\"text-md-left\"><li><strong>");
                ret.append(attribute.getName());
                ret.append(":</strong> " );
                ret.append(attribute.getValue_name());
                ret.append("</li></ul>");
            }
            ret.append("</p><p>&nbsp;</p><p>&nbsp;</p>");
        }

        return ret.toString();
    }

    private static List<String> getExcludedAttributeIds() {
        List<String> ret = new ArrayList<>();
        ret.add("BRAND");
        ret.add("GTIN");
        ret.add("IMPORT_DUTY");
        ret.add("VALUE_ADDED_TAX");
        ret.add("MANUFACTURING_TIME");
        ret.add("SELLER_PACKAGE_HEIGHT");
        ret.add("SELLER_PACKAGE_LENGTH");
        ret.add("SELLER_PACKAGE_TYPE");
        ret.add("SELLER_PACKAGE_WEIGHT");
        ret.add("SELLER_PACKAGE_WIDTH");
        ret.add("PRODUCTION_COMPANY");
        return ret;
    }

    private Image extractImage(Picture picture) {
        Image image = new Image();
        image.setSrc(picture.getSecure_url());
        return image;
    }

    /**
     * getProductsToSynch
     */
    private List<Listing> getListingsToSynch() {
        String filename = AppProperties.getInstance().getProperty(PRODUCTS_TO_SYNCH_FILE);
        InputDataReader reader = new InputDataReader();
        return reader.read(filename);
    }

}
