package com.anitsuga.robot.types;

import org.junit.Assert;
import org.junit.Test;

/**
 * WebScraperURLProviderTest
 * @author agustina
 *
 */
public class WebScraperURLProviderTest {

    @Test
    public void testGetAmazonId(){
        String url = "https://www.amazon.com/-/es/Dmitry-Glukhovsky/dp/1473204305/ref=sr_1_121?qid=1582864494&refinements=p_n_feature_browse-bin%3A2656022011&rnid=618072011&s=books&sr=1-121";
        WebScraperURLProvider robot = new WebScraperURLProvider("","");
        String id = robot.getAmazonId(url);
        boolean assertValue = "1473204305".equals(id);
        Assert.assertTrue(assertValue);
    }
}
