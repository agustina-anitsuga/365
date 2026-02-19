package com.anitsuga.fwk.utils;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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
            HttpPost httpPost = new HttpPost(url); // http post request

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


    /**
     * postJson
     * @param url
     * @return
     */
    public String postJson(String url, Map<String, String> headerMap, Map<String,String> parameters ){
        String ret = null;
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url); // http get request
            //System.out.println("Url: "+url);

            // add header
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                httpPost.addHeader(entry.getKey(), entry.getValue());
            }

            // add parameters
            ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            for (String key: parameters.keySet()) {
                postParameters.add(new BasicNameValuePair(key, parameters.get(key)));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(postParameters,"UTF-8"));

            // execute post
            CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpPost); // h
            int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();

            // check response
            //System.out.println("Status code: "+statusCode);
            if( statusCode == 200 ) {
                String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
                //System.out.println("Response: "+responseString);
                ret = responseString;
            }
            if(statusCode == 400){
                throw new RuntimeException("Bad request.");
            }
        } catch(Exception e){
            ret = null;
            LOGGER.error(e.getMessage(),e.getStackTrace());
        }
        return ret;
    }

    public String patchJson(String url, Map<String, String> headerMap, String body) {
        String ret = null;
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPatch httpPatch = new HttpPatch(url); // http patch request
            //System.out.println("Url: "+url);

            // add header
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                httpPatch.addHeader(entry.getKey(), entry.getValue());
            }

            // add body
            StringEntity entity = new StringEntity(body, ContentType.APPLICATION_JSON);
            httpPatch.setEntity(entity);

            // execute patch
            CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpPatch);
            int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();

            // check response
            //System.out.println("Status code: "+statusCode);
            if( statusCode == 200 ) {
                String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
                //System.out.println("Response: "+responseString);
                ret = responseString;
            }
            if(statusCode == 400){
                throw new RuntimeException("Bad request.");
            }
        } catch(Exception e){
            ret = null;
            LOGGER.error(e.getMessage(),e.getStackTrace());
        }
        return ret;
    }


    public String putJson(String url, Map<String, String> headerMap, String body) {
        String ret = null;
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPut httpPut = new HttpPut(url); // http put request
            //System.out.println("Url: "+url);

            // add header
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                httpPut.addHeader(entry.getKey(), entry.getValue());
            }

            // add body
            StringEntity entity = new StringEntity(body, ContentType.APPLICATION_JSON);
            httpPut.setEntity(entity);

            // execute patch
            CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpPut);
            int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();

            // check response
            //System.out.println("Status code: "+statusCode);
            if( statusCode == 200 ) {
                String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
                //System.out.println("Response: "+responseString);
                ret = responseString;
            }
            if(statusCode == 400){
                throw new RuntimeException("Bad request.");
            }
        } catch(Exception e){
            ret = null;
            LOGGER.error(e.getMessage(),e.getStackTrace());
        }
        return ret;
    }


    /**
     * delete
     * @param url
     * @return
     */
    public String delete(String url, Map<String, String> headerMap){
        String ret = null;
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpDelete httpPost = new HttpDelete(url); // http delete request

            // add header
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                httpPost.addHeader(entry.getKey(), entry.getValue());
            }

            // execute delete
            CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpPost); // h
            int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();

            // check response
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
}
