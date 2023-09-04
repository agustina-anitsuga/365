package com.anitsuga.invoicer.api;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * MeliRestClientTest
 */
public class MeliRestClientTest {

    private String orderId = "2000006332896030";


    @Test
    public void testAddNote(){
        MeliRestClient client = new MeliRestClient();
        Note note = client.addNote(orderId,"test");
        Assert.assertNotNull(note);
    }

    @Test
    public void testGetNotes(){
        MeliRestClient client = new MeliRestClient();
        List<Note> notes = client.getNotes(orderId);
        Assert.assertNotNull(notes);
    }

    @Test
    public void testGetOrderDetails(){
        MeliRestClient client = new MeliRestClient();
        List<OrderItem> details = client.getOrderDetails(orderId);
        Assert.assertNotNull(details);
    }

    @Test
    public void testGetBillingInfo(){
        MeliRestClient client = new MeliRestClient();
        BillingInfo info = client.getBillingInfo(orderId);
        Assert.assertNotNull(info);
    }

}
