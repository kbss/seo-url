package com.stylight.seo;

import com.stylight.seo.application.CacheConfiguration;
import com.stylight.seo.domain.UrlService;
import com.stylight.seo.util.AssertionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
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
        String url = "/Sun-Hats/Outdoor/";
        String expectedResult = "/products?brand=1527&tag=51";

        assertCacheIsNull(url, CacheConfiguration.PRETTY_URL);

        Map<String, String> prettyUrls = service.getParametrizedUrl(Collections.singletonList(url));

        AssertionUtils.assertValidSingleResult(expectedResult, prettyUrls);
        String cachedValue = assertCacheFilled(url, CacheConfiguration.PRETTY_URL);
        Assertions.assertNotNull(cachedValue);
        Assertions.assertEquals(expectedResult, cachedValue);
    }

    private String assertCacheFilled(String url, String prettyUrl) {
        Cache cache = cacheManager.getCache(prettyUrl);
        Assertions.assertNotNull(cache);
        return cache.get(url, String.class);
    }

    @Test
    public void testParametrizedUrlCache() {
        String url = "/products?brand=579&color=30&tag=119";
        String expectedResult = "/Lilly-Pulitzer/Short-Pants/Pink/";

        assertCacheIsNull(url, CacheConfiguration.PARAMETRIZED_URL);

        Map<String, String> prettyUrls = service.getPrettyUrls(Collections.singletonList(url));
        AssertionUtils.assertValidSingleResult(expectedResult, prettyUrls);
        String cachedValue = assertCacheFilled(url, CacheConfiguration.PARAMETRIZED_URL);
        Assertions.assertNotNull(cachedValue);
        Assertions.assertEquals(expectedResult, cachedValue);
    }

    private void assertCacheIsNull(String url, String parametrizedUrl) {
        Cache cache = cacheManager.getCache(parametrizedUrl);
        Assertions.assertNotNull(cache);
        String cachedValueBeforeCall = cache.get(url, String.class);
        Assertions.assertNull(cachedValueBeforeCall);
    }
}
