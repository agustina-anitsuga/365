package com.anitsuga.shop;

import com.anitsuga.fwk.utils.AppProperties;
import com.anitsuga.fwk.utils.FileUtils;
import com.anitsuga.shop.api.nube.NubeRestClient;
import com.anitsuga.shop.api.nube.model.*;
import com.anitsuga.shop.lambda.CategoryAddition;
import com.anitsuga.shop.lambda.NubeLambdaFunction;
import com.anitsuga.shop.model.Listing;
import com.anitsuga.shop.reader.InputDataReader;
import com.anitsuga.shop.writer.ResultExcelWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class ShopLambdaEditor {

    /**
     * PRODUCTS_TO_EDIT_FILE
     */
    protected static final String PRODUCTS_TO_EDIT_FILE = "products.to.edit.file";

    /**
     * Client to Nube
     */
    private final NubeRestClient nubeClient = new NubeRestClient();

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ShopLambdaEditor.class.getName());

    public ShopLambdaEditor(){
    }

    /**
     * main
     */
    public static void main(String[] args) {
        ShopLambdaEditor self = new ShopLambdaEditor();
        self.run(new CategoryAddition());
    }

    /**
     * run
     */
    public void run( NubeLambdaFunction function ){

        List<Listing> listings = getProductsToEdit();

        int count = 0;
        int total = listings.size();
        for (Listing listing : listings) {
            LOGGER.info("Editing list item "+listing.getMeliId()+ " ["+(++count)+"/"+total+"]");
            Instant start = Instant.now();
            String result = processProduct(listing, function);
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
     * writeResults
     */
    private void writeResults(List<Listing> listings) {
        String localPath = FileUtils.getLocalPath();
        String filename = localPath + outputFilePrefix();
        ResultExcelWriter writer = new ResultExcelWriter();
        writer.write(filename, listings);
        LOGGER.info("Finished invoicing job");
    }

    /**
     * outputFilePrefix
     */
    private String outputFilePrefix() {
        return "edited-products";
    }

    /**
     * processListing
     */
    private String processProduct(Listing listing, NubeLambdaFunction function ) {
        String ret = "";
        try {

            // Look up listing in tienda nube
            Product product = nubeClient.getProductBySKU(listing.getMeliId());

            // Edit Products
            Product resultingProduct = function.apply(product);

            // verify edit - TODO improve
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

    /**
     * getProductsToEdit
     */
    private List<Listing> getProductsToEdit() {
        String filename = AppProperties.getInstance().getProperty(PRODUCTS_TO_EDIT_FILE);
        InputDataReader reader = new InputDataReader();
        return reader.read(filename);
    }

}
