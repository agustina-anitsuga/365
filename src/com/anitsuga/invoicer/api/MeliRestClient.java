package com.anitsuga.invoicer.api;

import com.anitsuga.fwk.utils.AppProperties;
import com.anitsuga.fwk.utils.RestClient;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MeliRestClient
 */
public class MeliRestClient {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MeliRestClient.class.getName());

    private static final RestClient restClient = new RestClient();

    /**
     * getOrder
     * @param orderId
     * @return
     */
    public Order getOrder(String orderId ){
        return this.getOrder(orderId,true);
    }

    private Order getOrder(String orderId, boolean checkPackIfNull ){
        Order ret = null;
        if( orderId != null ) {
            String response = restClient.get("https://api.mercadolibre.com/orders/" + orderId.trim(), buildHeader());
            if (response != null) {
                Order order = new Gson().fromJson(response, Order.class);
                if (orderId.equals(order.getId()))
                    ret = order;
            } else if (checkPackIfNull) {
                Pack pack = getPack(orderId);
                for (Order o: pack.getOrders()) {
                    String oID = o.getId();
                    Order temp = this.getOrder(oID, false);
                    if( ret==null ){
                        ret = temp;
                        ret.setId(orderId);
                    } else {
                        ret.setFulfilled( ret.isFulfilled() && temp.isFulfilled() );
                        ret.getOrder_items().addAll(temp.getOrder_items());
                        ret.getTags().addAll(temp.getTags());
                        if(ret.getCancel_details()==null){
                            ret.setCancel_details(temp.getCancel_details());
                        }
                        if(ret.getOrder_request().getChange()==null){
                            ret.getOrder_request().setChange(temp.getOrder_request().getChange());
                        }
                        if(ret.getOrder_request().getReturn()==null){
                            ret.getOrder_request().setReturn(temp.getOrder_request().getReturn());
                        }
                        if(temp.getStatus()!="paid"){
                            ret.setStatus(temp.getStatus());
                        }
                    }
                }
            }
        }
        return ret;
    }

    private String getOrderIdFromPack(Pack pack) {
        String ret = null;
        if( pack!=null && pack.getOrders().size()>=1 ){
            ret = pack.getOrders().get(0).getId();
        }
        return ret;
    }

    private Pack getPack(String orderId) {
        Pack pack = null;
        String response = restClient.get("https://api.mercadolibre.com/packs/"+ orderId.trim(), buildHeader());
        if( response!=null ) {
            pack = new Gson().fromJson(response, Pack.class);
        }
        return pack;
    }

    /**
     * addNote
     * @param orderId
     * @param note
     * @return
     */
    public Note addNote(String orderId, String note ){
        return this.addNote(orderId,note,true);
    }

    public Note addNote(String orderId, String note, boolean checkPackIfNull ){
        Note ret = null;
        if(orderId!=null) {
            String json = "{ \"note\": \"" + note + "\" }";
            String response = restClient.postJson("https://api.mercadolibre.com/orders/" + orderId.trim() + "/notes", buildHeader(), json);
            if (response != null) {
                NoteContainer notes = new Gson().fromJson(response, NoteContainer.class);
                ret = notes.getNote();
            } else if (checkPackIfNull) {
                Pack pack = this.getPack(orderId);
                String packOrderId = this.getOrderIdFromPack(pack);
                ret = this.addNote(packOrderId, note, false);
            }
        }
        return ret;
    }

    /**
     * getNotes
     * @param orderId
     * @return
     */
    public List<Note> getNotes(String orderId ){
        return this.getNotes(orderId,true);
    }

    /**
     * getNotes
     * @param orderId
     * @return
     */
    private List<Note> getNotes(String orderId, boolean checkPackIfNull ){
        List<Note> ret = null;
        if(orderId!=null) {
            String response = restClient.get("https://api.mercadolibre.com/orders/" + orderId.trim() + "/notes", buildHeader());
            if (response != null) {
                response = "{" + response.substring(2, response.length() - 2) + " }";
                Notes notes = new Gson().fromJson(response, Notes.class);
                if (orderId.equals(notes.getOrder_id()))
                    ret = notes.getResults();
            } else if (checkPackIfNull) {
                Pack pack = this.getPack(orderId);
                String packOrderId = this.getOrderIdFromPack(pack);
                ret = this.getNotes(packOrderId, false);
            }
        }
        return ret;
    }

    /**
     * getBillingInfo
     * @param orderId
     * @return
     */
    public BillingInfo getBillingInfo(String orderId ){
        return this.getBillingInfo(orderId,true);
    }

    /**
     * getBillingInfo
     * @param orderId
     * @return
     */
    private BillingInfo getBillingInfo(String orderId, boolean checkPackIfNull ){
        BillingInfo ret = null;
        if(orderId!=null) {
            String response = restClient.get("https://api.mercadolibre.com/orders/" + orderId.trim() + "/billing_info", buildHeader());
            if (response != null) {
                OrderBillingInfo bi = new Gson().fromJson(response, OrderBillingInfo.class);
                ret = bi.getBilling_info();
            } else if (checkPackIfNull) {
                Pack pack = this.getPack(orderId);
                String packOrderId = this.getOrderIdFromPack(pack);
                ret = this.getBillingInfo(packOrderId, false);
            }
        }
        return ret;
    }

    public boolean isInvoiced(String orderId){
        return this.isInvoiced(orderId,true);
    }

    private boolean isInvoiced(String orderId, boolean checkPackIfNull){
        boolean ret = false;
        if(orderId!=null) {
            String response = restClient.get("https://api.mercadolibre.com/packs/" + orderId.trim() + "/fiscal_documents", buildHeader());
            if (response != null) {
                ret = true;
            } else if (checkPackIfNull) {
                Pack pack = this.getPack(orderId);
                String packOrderId = this.getOrderIdFromPack(pack);
                ret = this.isInvoiced(packOrderId, false);
            }
        }
        return ret;
    }

    /**
     * buildHeader
     * @return
     */
    private Map<String,String> buildHeader () {
        Map<String,String> header = new HashMap<String,String>();
        header.put("Content-Type", "application/json; charset=UTF-8");
        header.put("Authorization", "Bearer "+ AppProperties.getInstance().getProperty("api.token"));
        return header;
    }
}
