package com.stylight.seo;

import com.stylight.seo.domain.UrlService;
import com.stylight.seo.domain.exception.NullUrlException;
import com.stylight.seo.repository.InMemoryRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@ExtendWith(SpringExtension.class)
@Import(ServiceTestConfiguration.class)
public class UrlServiceTest {

    private Logger log = LoggerFactory.getLogger(UrlServiceTest.class);
    @Autowired
    private UrlService service;

    @Autowired
    private InMemoryRepository inMemoryRepository;

    @Test
    public void testPartialSearch() {
        String url = "/products?gender=female&tag=123&tag=1234&tag=5678";
        Map<String, String> prettyUrls = service.getPrettyUrls(Collections.singletonList(url));
        assertValidResult("/Women/Shoes/?tag=5678", prettyUrls);
    }

    @Test
    public void testPartialSearchSingleSlash() {
        String url = "/";
        Map<String, String> prettyUrls = service.getPrettyUrls(Collections.singletonList(url));
        assertValidResult(url, prettyUrls);
    }

    @Test
    public void testPartialSearchEmptyUrl() {
        String url = StringUtils.EMPTY;
        Map<String, String> prettyUrls = service.getPrettyUrls(Collections.singletonList(url));
        assertValidResult(url, prettyUrls);
    }

    @Test
    public void testPrettyNullUrl() {
        Assertions.assertThrows(NullUrlException.class, () -> service.getPrettyUrls(Collections.singletonList(null)));
    }

    @Test
    public void testParametrizedUrl() {
        Assertions.assertThrows(NullUrlException.class, () -> service.getFullUrl(Collections.singletonList(null)));
    }

    @Test
    public void testPartialSearch2() {
        String url = "/products?gender=female&tag=123&tag=1234&tag=5678&tag=9877";
        Map<String, String> prettyUrls = service.getPrettyUrls(Collections.singletonList(url));
        assertValidResult("/Women/Shoes/?tag=5678&tag=9877", prettyUrls);
    }

    @Test
    public void testNonExistingUrl() {
        String url = "/orders?gender=female";
        Map<String, String> prettyUrls = service.getPrettyUrls(Collections.singletonList(url));
        assertValidResult(url, prettyUrls);
    }

    @Test
    public void testHalfUrlCover() {
        String url = "/products?gender=female&tag=x123&tag=x1234&tag=x5678";
        Map<String, String> prettyUrls = service.getPrettyUrls(Collections.singletonList(url));
        assertValidResult(url, prettyUrls);
    }


    @Test
    public void testPrettyUrlSearch() {
        String url = "/Women/Shoes/";
        Map<String, String> prettyUrls = service.getFullUrl(Collections.singletonList(url));
        assertValidResult("/products?gender=female&tag=123&tag=1234", prettyUrls);
    }

    @Test
    public void testPartialPrettyUrlSearch() {
        String url = "/Women/Shoes/?tag=5678";
        Map<String, String> prettyUrls = service.getFullUrl(Collections.singletonList(url));
        assertValidResult("/products?gender=female&tag=5678", prettyUrls);
    }

    private Map<String, String> invert(Map<String, String> map) {
        return map.entrySet().stream().collect(Collectors.toMap(e -> e.getValue(), e -> e.getKey()));
    }

    @Test
    public void testGetPrettyUrls() {
        Map<String, String> all = inMemoryRepository.findAll();
        testAllOneByOne(all, p -> service.getPrettyUrls(p));
    }


    @Test
    public void testGetParametrizedUrls() {
        Map<String, String> all = inMemoryRepository.findAll();

        testAllOneByOne(invert(all), p -> service.getFullUrl(p));
    }

    @Test
    public void testGetPrettyUrlsAllAtOnce() {
        Map<String, String> all = inMemoryRepository.findAll();
        testAllAtOnce(all, p -> service.getPrettyUrls(p));
    }


    @Test
    public void testGetParametrizedUrlsAllAtOnce() {
        Map<String, String> all = inMemoryRepository.findAll();
        log.info("All: {}", all);
        testAllAtOnce(invert(all), p -> service.getFullUrl(p));
    }

    private void testAllOneByOne(Map<String, String> urlsMap, Function<List<String>, Map<String, String>> function) {
        urlsMap.entrySet().forEach(e -> {
            String parametrizedUrl = e.getKey();
            String expectedResult = e.getValue();
            log.info(" {} -> {}", parametrizedUrl, expectedResult);
            Map<String, String> urls = function.apply(Collections.singletonList(parametrizedUrl));
            assertValidResult(expectedResult, urls);
        });
    }

    private void testAllAtOnce(Map<String, String> urlsMap, Function<Collection<String>, Map<String, String>> function) {
        Collection<String> values = urlsMap.keySet();
        Map<String, String> urls = function.apply(values);

        log.info(" {} ", urls);
        urlsMap.entrySet().forEach(e -> {
            String key = e.getKey();
            String resultUrl = urls.get(key);
            Assertions.assertNotNull(resultUrl);
            Assertions.assertEquals(e.getValue(), resultUrl);
            Assertions.assertEquals(e.getValue(), resultUrl);
        });
    }


    private void assertValidResult(String expectedResult, Map<String, String> urls) {
        Iterator<Map.Entry<String, String>> iterator = urls.entrySet().iterator();
        Assertions.assertTrue(iterator.hasNext());
        Map.Entry<String, String> next = iterator.next();
        Assertions.assertEquals(expectedResult, next.getValue());
    }
}
