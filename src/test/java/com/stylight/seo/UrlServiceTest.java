package com.stylight.seo;

import com.stylight.seo.domain.UrlService;
import com.stylight.seo.domain.exception.UrlValidationException;
import com.stylight.seo.repository.InMemoryRepository;
import com.stylight.seo.util.AssertionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;


@SpringBootTest
public class UrlServiceTest {

    private final Logger log = LoggerFactory.getLogger(UrlServiceTest.class);
    @Autowired
    private UrlService service;

    @Autowired
    private InMemoryRepository inMemoryRepository;

    @Test
    public void testPartialSearch() {
        String url = "/products?brand=427&tag=12296&tag=5678";
        Map<String, String> prettyUrls = service.getPrettyUrls(Collections.singletonList(url));
        AssertionUtils.assertValidSingleResult("/Givenchy/?tag=12296&tag=5678", prettyUrls);
    }

    @Test
    public void testPartialSearchSingleSlash() {
        String url = "/";
        Map<String, String> prettyUrls = service.getPrettyUrls(Collections.singletonList(url));
        AssertionUtils.assertValidSingleResult(url, prettyUrls);
    }

    @Test
    public void testPartialSearchEmptyUrl() {
        String url = StringUtils.EMPTY;
        Map<String, String> prettyUrls = service.getPrettyUrls(Collections.singletonList(url));
        AssertionUtils.assertValidSingleResult(url, prettyUrls);
    }

    @Test
    public void testPrettyNullUrl() {
        Assertions.assertThrows(UrlValidationException.class, () -> service.getPrettyUrls(Collections.singletonList(null)));
    }

    @Test
    public void testParametrizedUrl() {
        Assertions.assertThrows(UrlValidationException.class, () -> service.getParametrizedUrl(Collections.singletonList(null)));
    }

    @Test
    public void testPartialSearch2() {
        String url = "/products?brand=4757&tag=6&tag=5678&tag=9877";
        Map<String, String> prettyUrls = service.getPrettyUrls(Collections.singletonList(url));
        AssertionUtils.assertValidSingleResult("/Asyou/Dresses/?tag=5678&tag=9877", prettyUrls);
    }

    @Test
    public void testGetPrettyUlrByPrettyUrl() {
        String url = "/Asyou/Dresses/";
        Map<String, String> prettyUrls = service.getPrettyUrls(Collections.singletonList(url));
        AssertionUtils.assertValidSingleResult("/Asyou/Dresses/", prettyUrls);
    }

    @Test
    public void testNonExistingUrl() {
        String url = "/orders?gender=female";
        Map<String, String> prettyUrls = service.getPrettyUrls(Collections.singletonList(url));
        AssertionUtils.assertValidSingleResult(url, prettyUrls);
    }

    @Test
    public void testHalfUrlCover() {
        String url = "/products?gender=female&tag=x123&tag=x1234&tag=x5678";
        Map<String, String> prettyUrls = service.getPrettyUrls(Collections.singletonList(url));
        AssertionUtils.assertValidSingleResult(url, prettyUrls);
    }


    @Test
    public void testPrettyUrlSearch() {
        String url = "/Summer-Pants/Gray/Hipster/";
        Map<String, String> prettyUrls = service.getParametrizedUrl(Collections.singletonList(url));
        AssertionUtils.assertValidSingleResult("/products?brand=784&color=24&tag=43", prettyUrls);
    }

    @Test
    public void testPartialPrettyUrlSearch() {
        String url = "/The-Addams-Family/Clothing/?tag=5678";
        Map<String, String> resultUrl = service.getParametrizedUrl(Collections.singletonList(url));
        AssertionUtils.assertValidSingleResult("/products?brand=4756&tag=4&tag=5678", resultUrl);
    }

    private Map<String, String> invert(Map<String, String> map) {
        return map.entrySet().stream().limit(200).collect(Collectors.toMap(e -> e.getValue(), e -> e.getKey()));
    }

    @Test
    public void testGetPrettyUrls() {
        Map<String, String> all = inMemoryRepository.findAll().entrySet().stream().limit(10000).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
        testAllOneByOne(all, p -> service.getPrettyUrls(p));
    }


    @Test
    public void testGetParametrizedUrls() {
        Map<String, String> all = inMemoryRepository.findAll();

        testAllOneByOne(invert(all), p -> service.getParametrizedUrl(p));
    }


    @Test
    public void testGetPrettyUrlsBatch() {
        Map<String, String> all = inMemoryRepository.findAll();
        batchTest(all, p -> service.getPrettyUrls(p));
    }

    @Test
    public void testGetParametrizedUrlsBatch() {
        Map<String, String> all = inMemoryRepository.findAll();
        batchTest(invert(all), p -> service.getParametrizedUrl(p));
    }

    private void testAllOneByOne(Map<String, String> urlsMap, Function<List<String>, Map<String, String>> function) {
        urlsMap.entrySet().forEach(e -> {
            String parametrizedUrl = e.getKey();
            String expectedResult = e.getValue();
            Map<String, String> urls = function.apply(Collections.singletonList(parametrizedUrl));
            AssertionUtils.assertValidSingleResult(expectedResult, urls);
        });
    }

    private void batchTest(Map<String, String> urlsMap, Function<Collection<String>, Map<String, String>> function) {
        int requestSize = 100;
        int shift = ThreadLocalRandom.current().nextInt(urlsMap.size() - requestSize);
        Collection<String> keys = urlsMap.keySet().stream().skip(shift).limit(requestSize).collect(Collectors.toList());
        Map<String, String> urls = function.apply(keys);

        assertEach(urlsMap, keys, urls);
    }

    private void assertEach(Map<String, String> urlsMap, Collection<String> keys, Map<String, String> urls) {
        keys.forEach(key -> {
            String resultUrl = urls.get(key);
            Assertions.assertNotNull(resultUrl);
            Assertions.assertEquals(urlsMap.get(key), resultUrl);
        });
    }


}
