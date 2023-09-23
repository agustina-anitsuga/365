package com.anitsuga.fwk.utils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
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
            //System.out.println("Url: "+url);
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                httpget.addHeader(entry.getKey(), entry.getValue());
            }

            CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpget); // h
            int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
            //System.out.println("Status code: "+statusCode);
            if( statusCode == 200 ) {
                String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
                //System.out.println("Response: "+responseString);
                ret = responseString;
            }
            if(statusCode == 401){
                throw new RuntimeException("Unauthorized access.");
            }
        } catch(Exception e){
            ret = null;
            LOGGER.error(e.getMessage(),e.getStackTrace());
        }
        return ret;
    }

    /**
     * postJson
     * @param url
     * @return
     */
    public String postJson(String url, Map<String, String> headerMap, String body ){
        String ret = null;
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url); // http get request
            //System.out.println("Url: "+url);

            // add header
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                httpPost.addHeader(entry.getKey(), entry.getValue());
            }

            // add body
            StringEntity entity = new StringEntity(body, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);

            // execute post
            CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpPost); // h
            int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();

            // check response
            //System.out.println("Status code: "+statusCode);
            if( statusCode == 201 ) {
                String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
                //System.out.println("Response: "+responseString);
                ret = responseString;
            }
            if(statusCode == 401){
                throw new RuntimeException("Unauthorized access.");
            }
        } catch(Exception e){
            ret = null;
            LOGGER.error(e.getMessage(),e.getStackTrace());
        }
        return ret;
    }
}
