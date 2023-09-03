package com.anitsuga.invoicer;

import com.anitsuga.invoicer.api.MeliRestClient;
import com.anitsuga.invoicer.api.Note;
import com.anitsuga.invoicer.model.Sale;

import java.util.List;

/**
 * WebInvoiceStatusChecker
 */
public class ApiInvoiceStatusChecker extends InvoiceStatusChecker {

    /**
     * getSaleStatus
     * @param sale
     * @return
     */
    protected String getSaleStatus(Sale sale) {
        MeliRestClient client = new MeliRestClient();

        String ret = "Invoice Pending";
        List<Note> notes = client.getNotes(sale.getId());
        if (notes != null) {
            for (Note note : notes) {
                if (note.getNote().contains("F")) {
                    ret = "Already invoiced";
                    break;
                }
            }
        }
        return ret;
    }

    protected void sleep() {
        // do nothing
    }
}
