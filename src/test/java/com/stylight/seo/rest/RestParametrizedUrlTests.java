package com.stylight.seo.rest;

import com.stylight.seo.util.AssertionUtils;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RestParametrizedUrlTests extends BaseRestTest {

    @Test
    public void prettyUrlSingleUrlNoMatchTest() {
        String expected = "/Pipinkor/Shoesu/Black/";
        getParametrizedUrl(expected, expected);
    }

    @Test
    public void prettyUrlSingleUrlFullMatchTest() {
        String url = "/products?brand=19&color=21&tag=18";
        String expected = "/Patent-Leather-Shoes/White/Business/";
        getParametrizedUrl(url, expected);
    }

    @Test
    public void prettyUrlSingleUrlPartialMatchTest() {
        String url = "/Fiorelli/Handbags/Black/";
        String expected = "/products?brand=4357&color=18&tag=11";
        getParametrizedUrl(url, expected);
    }

    private void getParametrizedUrl(String url, String expected) {
        List<String> strings = Collections.singletonList(url);
        Map<String, String> prettyUrls = getResource().getParametrizedUrls(strings);
        AssertionUtils.assertValidSingleResult(url, expected, prettyUrls);
    }
}
