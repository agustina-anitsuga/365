package com.anitsuga.shop;

import com.anitsuga.fwk.utils.AppProperties;
import com.anitsuga.fwk.utils.FileUtils;
import com.anitsuga.shop.api.meli.MeliRestClient;
import com.anitsuga.shop.api.meli.model.*;
import com.anitsuga.shop.model.Listing;
import com.anitsuga.shop.writer.ResultExcelWriter;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class MeliListingRetriever {

    /**
     * Client to Meli
     */
    private final MeliRestClient meliClient;

    private int mycount = 0;

    public MeliListingRetriever(){
        meliClient = new MeliRestClient();
    }

    /**
     * main
     */
    public static void main(String[] args) {
        MeliListingRetriever self = new MeliListingRetriever();
        self.run();
    }

    /**
     * run
     */
    public void run(){

        List<Listing> listings = getListingsToSynch();
        List<Listing> filteredListings = listings.stream()
                .filter(this::shouldKeep)
                .collect(Collectors.toList());
        writeResults(filteredListings);
    }

    private boolean shouldKeep(Listing listing) {
        System.out.println("Looking for item "+listing.getMeliId()+" - "+(++mycount));
        Item item = meliClient.getItemById(listing.getMeliId());
        if( item!=null ) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MMM/dd");
            String formatted = sdf.format(item.getLast_updated());
            listing.setResult(item.getStatus() + " " + formatted);
        }
        // return !"closed".equals(item.getStatus()) ;
        return true;
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
        return "meli-listings";
    }


    /**
     * getProductsToSynch
     */
    private List<Listing> getListingsToSynch() {
        String userId = AppProperties.getInstance().getProperty("api.user_id");
        List<Listing> ret = meliClient.getAllPublicationIds(userId);
        return ret;
    }

}
