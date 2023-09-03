package com.anitsuga.fwk.utils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * RestClient
 */
public class RestClient {


    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RestClient.class.getName());

    /**
     * get
     * @param url
     * @return
     */
    public String get( String url, Map<String, String> headerMap ){
        String ret = null;
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(url); // http get request
            System.out.println("Url: "+url);
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                httpget.addHeader(entry.getKey(), entry.getValue());
            }
            CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpget); // h
            int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
            System.out.println("Status code: "+statusCode);
            if( statusCode == 200 ) {
                String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
                System.out.println("Response: "+responseString);
                ret = responseString;
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return ret;
    }

}
