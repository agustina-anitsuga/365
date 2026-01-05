package com.anitsuga.shop;

import com.anitsuga.fwk.utils.AppProperties;
import com.anitsuga.shop.api.meli.MeliRestClient;
import com.anitsuga.shop.api.meli.model.Item;
import com.anitsuga.shop.api.meli.model.ItemDescription;
import com.anitsuga.shop.model.Listing;
import com.anitsuga.shop.reader.InputDataReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
            System.out.println("Synchronizing list item "+listing.getId()+ " ["+(++count)+"/"+total+"]");
            String result = synch(listing);
            listing.setResult(result);
            System.out.println(result);
            System.out.println("---");
        }

        //writeResults(products);
    }

    /**
     * writeResults
     */
    private void writeResults(List<Listing> sales) {
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
     * @param product
     * @return
     */
    private String synch(Listing product) {
        String ret = "";
        try {
            MeliRestClient client = new MeliRestClient();

            // Get data from MercadoLibre
            Item item = client.getItemById(product.getId());
            ItemDescription id = client.getItemDescriptionById(product.getId());

            // Look up listing in tienda nube

            // If listing exists, update price and stock

            // If listing does not exist, create it


        } catch (Exception e) {
            ret = e.getMessage();
        }
        return ret;
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
