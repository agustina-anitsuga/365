package com.anitsuga.invoicer;

import com.anitsuga.invoicer.api.MeliRestClient;
import com.anitsuga.invoicer.api.Token;

public class ApiAccessTokenRetriever {

    public static void main(String[] args) {
        MeliRestClient client = new MeliRestClient();
        Token token = client.getToken();
        if(token!=null)
            System.out.println(token.getAccess_token());
    }

}
