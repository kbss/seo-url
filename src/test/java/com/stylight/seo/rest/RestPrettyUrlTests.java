package com.stylight.seo.rest;

import com.stylight.seo.util.AssertionUtils;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RestPrettyUrlTests extends BaseRestTest {

    @Test
    public void prettyUrlSingleUrlNoMatchTest() {
        String expected = "/orders?gender=female";
        getPrettyUrl(expected, expected);
    }

    @Test
    public void prettyUrlSingleUrlFullMatchTest() {
        String url = "/products?brand=19&color=21&tag=18";
        String expected = "/Patent-Leather-Shoes/White/Business/";
        getPrettyUrl(url, expected);
    }

    @Test
    public void prettyUrlSingleUrlPartialMatchTest() {
        String url = "/products?brand=19&color=21&tag=18&tag=9999";
        String expected = "/Patent-Leather-Shoes/White/Business/?tag=9999";
        getPrettyUrl(url, expected);
    }

    private void getPrettyUrl(String url, String expected) {
        List<String> strings = Collections.singletonList(url);
        Map<String, String> prettyUrls = getResource().getPrettyUrls(strings);
        AssertionUtils.assertValidSingleResult(url, expected, prettyUrls);
    }
}
