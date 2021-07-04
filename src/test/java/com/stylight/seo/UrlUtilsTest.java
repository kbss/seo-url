package com.stylight.seo;

import com.stylight.seo.service.UrlUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UrlUtilsTest {

    @Test
    public void testUrlSplit() {
        List<String> expectedResult = new ArrayList<>() {{
            add("/products");
            add("brand=4756");
            add("tag=17648");
        }};
        List<String> urlParts = UrlUtils.splitParametrizedUrl("/products?brand=4756&tag=17648");
        assertEquals(expectedResult, urlParts);
    }

    @Test
    public void testPrettyUrlSplit() {
        List<String> expectedResult = new ArrayList<>() {{
            add("/Crocs/Low-Top-Sneakers/");
            add("tag=17648");
        }};
        List<String> urlParts = UrlUtils.splitParametrizedUrl(expectedResult.stream().collect(Collectors.joining("?")) + "&");
        assertEquals(expectedResult, urlParts);
    }


    private void assertEquals(List<String> expectedResult, List<String> urlParts) {
        for (int i = 0; i < expectedResult.size(); i++) {
            String expected = expectedResult.get(i);
            String actual = urlParts.get(i);
            Assertions.assertEquals(expected, actual);
        }
    }
}
