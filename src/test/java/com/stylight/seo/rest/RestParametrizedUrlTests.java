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
        String expected = "/products?brand=19&color=21&tag=18";
        getParametrizedUrl(url, expected);
    }

    @Test
    public void prettyUrlSingleUrlPartialMatchTest() {
        String url = "/Vans/Casual-T-Shirts/Blue/?tag=1001";
        String expected = "/products?brand=292&color=6&tag=41&tag=1001";
        getParametrizedUrl(url, expected);
    }

    private void getParametrizedUrl(String url, String expected) {
        List<String> strings = Collections.singletonList(url);
        Map<String, String> prettyUrls = getResource().getParametrizedUrls(strings);
        AssertionUtils.assertValidSingleResult(url, expected, prettyUrls);
    }
}
