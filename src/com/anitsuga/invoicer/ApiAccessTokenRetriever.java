package com.anitsuga.invoicer;

import com.anitsuga.invoicer.api.MeliRestClient;
import com.anitsuga.invoicer.api.Token;

public class ApiAccessTokenRetriever {

    public static void main(String[] args) {
        MeliRestClient client = new MeliRestClient();
        Token token = client.getToken();
        if(token!=null) {
            System.out.println("Access token:");
            System.out.println(token.getAccess_token());
            System.out.println("Refresh token:");
            System.out.println(token.getRefresh_token());
            /*
            Token longToken = client.refreshToken(token.getRefresh_token());
            System.out.println("Long-lived access token:");
            System.out.println(longToken.getAccess_token());
            System.out.println("Long-lived refresh token:");
            System.out.println(longToken.getRefresh_token());
            */
        }
    }

}
