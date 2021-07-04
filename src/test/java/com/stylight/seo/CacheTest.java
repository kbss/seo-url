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
        String url = "/products?brand=784&color=24&tag=43";
        Map<String, String> prettyUrls = service.getParametrizedUrl(Collections.singletonList(url));
        String expectedResult = "/Summer-Pants/Gray/Hipster/";
        AssertionUtils.assertValidSingleResult(expectedResult, prettyUrls);
        String cachedValue = cacheManager.getCache(CacheConfiguration.PARAMETRIZED_URL).get(url, String.class);
        Assertions.assertNotNull(cachedValue);
        Assertions.assertEquals(expectedResult, cachedValue);
    }
}
