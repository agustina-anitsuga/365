package com.anitsuga.invoicer.api;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * MeliRestClientTest
 */
public class MeliRestClientTest {

    private String orderId = "2000006440171252";
        // uploaded invoice "2000006440171252";
        // pack "2000004329269457";
        // regular "2000006332896030";


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
    public void testGetOrder(){
        MeliRestClient client = new MeliRestClient();
        Order order = client.getOrder(orderId);
        Assert.assertNotNull(order);
    }

    @Test
    public void testGetBillingInfo(){
        MeliRestClient client = new MeliRestClient();
        BillingInfo info = client.getBillingInfo(orderId);
        Assert.assertNotNull(info);
    }

    @Test
    public void testIsInvoiced(){
        MeliRestClient client = new MeliRestClient();
        boolean ret = client.isInvoiced(orderId);
        Assert.assertTrue(ret);
    }
}
