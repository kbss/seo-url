package com.stylight.seo;

import com.stylight.seo.domain.UrlService;
import com.stylight.seo.repository.InMemoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
@Import(ServiceTestConfiguration.class)
public class SeoUrlServiceTest {

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

    @Test
    public void testGetPrettyUrls() {
        Map<String, String> all = inMemoryRepository.findAll();
        testAll(all, p -> service.getPrettyUrls(p));
    }

    private Map<String, String> invert(Map<String, String> map){
        return map.entrySet().stream().collect(Collectors.toMap(e -> e.getValue(), e -> e.getKey()));
    }
    @Test
    public void testGetParametrizedUrls() {
        Map<String, String> all = inMemoryRepository.findAll();
        testAll(invert(all),p -> service.getFullUrl(p));
    }

    public void testAll(Map<String, String> urlsMap, Function<List<String>, Map<String, String>> function) {

        urlsMap.entrySet().forEach(e -> {
            String parametrizedUrl = e.getKey();
            String expectedResult = e.getValue();
            Map<String, String> urls = function.apply(Collections.singletonList(parametrizedUrl));
            assertValidResult(expectedResult, urls);
        });
    }


    private void assertValidResult(String expectedResult, Map<String, String> urls) {
        Iterator<Map.Entry<String, String>> iterator = urls.entrySet().iterator();
        Assertions.assertTrue(iterator.hasNext());
        Map.Entry<String, String> next = iterator.next();
        Assertions.assertEquals(expectedResult, next.getValue());
    }
}
