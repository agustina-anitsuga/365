package com.anitsuga.shop.api.nube;

import com.anitsuga.fwk.utils.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * NubeRestClient
 * https://dev.tiendanube.com/docs/applications/overview#autenticando-seu-aplicativo
 * https://dev.tiendanube.com/docs/developer-tools/nuvemshop-api
 */
public class NubeRestClient {


    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(NubeRestClient.class.getName());

    private static final RestClient restClient = new RestClient();

}
