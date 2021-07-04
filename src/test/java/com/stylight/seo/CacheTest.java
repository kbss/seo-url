package com.stylight.seo;

import com.stylight.seo.application.CacheConfiguration;
import com.stylight.seo.domain.UrlService;
import com.stylight.seo.util.AssertionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import java.util.Collections;
import java.util.Map;

@SpringBootTest
public class CacheTest {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private UrlService service;

    @Test
    public void testPrettyUrlCache() {
        String url = "/Summer-Pants/Gray/Hipster/";
        String expectedResult = "/products?brand=784&color=24&tag=43";

        assertCacheIsNull(url, CacheConfiguration.PRETTY_URL);

        Map<String, String> prettyUrls = service.getParametrizedUrl(Collections.singletonList(url));

        AssertionUtils.assertValidSingleResult(expectedResult, prettyUrls);
        String cachedValue = assertCacheFilled(url, CacheConfiguration.PRETTY_URL);
        Assertions.assertNotNull(cachedValue);
        Assertions.assertEquals(expectedResult, cachedValue);
    }

    private String assertCacheFilled(String url, String prettyUrl) {
        return cacheManager.getCache(prettyUrl).get(url, String.class);
    }

    @Test
    public void testParametrizedUrlCache() {
        String url = "/products?brand=520&tag=29&tag=97";
        String expectedResult = "/Gemstone-Rings/Gold/Wedding-Guest/";

        assertCacheIsNull(url, CacheConfiguration.PARAMETRIZED_URL);

        Map<String, String> prettyUrls = service.getPrettyUrls(Collections.singletonList(url));
        AssertionUtils.assertValidSingleResult(expectedResult, prettyUrls);
        String cachedValue = assertCacheFilled(url, CacheConfiguration.PARAMETRIZED_URL);
        Assertions.assertNotNull(cachedValue);
        Assertions.assertEquals(expectedResult, cachedValue);
    }

    private void assertCacheIsNull(String url, String parametrizedUrl) {
        String cachedValueBeforeCall = cacheManager.getCache(parametrizedUrl).get(url, String.class);
        Assertions.assertNull(cachedValueBeforeCall);
    }
}
