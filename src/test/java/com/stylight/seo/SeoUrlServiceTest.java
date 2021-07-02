package com.stylight.seo;

import com.stylight.seo.domain.UrlService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@Import(ServiceTestConfiguration.class)
public class SeoUrlServiceTest {

    @Autowired
    private UrlService service;

    @Test
    public void testPartialSearch() {
        String url = "/products?gender=female&tag=123&tag=1234&tag=5678";
        Map<String, String> prettyUrls = service.getPrettyUrls(Collections.singletonList(url));
        Iterator<Map.Entry<String, String>> iterator = prettyUrls.entrySet().iterator();
        Assertions.assertTrue(iterator.hasNext());
        Map.Entry<String, String> next = iterator.next();
        Assertions.assertEquals("/Women/Shoes/?tag=5678", next.getValue());
    }

    @Test
    public void testPartialSearch2() {
        String url = "/products?gender=female&tag=123&tag=1234&tag=5678&tag=9877";
        Map<String, String> prettyUrls = service.getPrettyUrls(Collections.singletonList(url));
        Iterator<Map.Entry<String, String>> iterator = prettyUrls.entrySet().iterator();
        Assertions.assertTrue(iterator.hasNext());
        Map.Entry<String, String> next = iterator.next();
        Assertions.assertEquals("/Women/Shoes/?tag=5678&tag=9877", next.getValue());
    }
    @Test
    public void testNonExistingUrl() {
        String url = "/orders?gender=female";
        Map<String, String> prettyUrls = service.getPrettyUrls(Collections.singletonList(url));
        Iterator<Map.Entry<String, String>> iterator = prettyUrls.entrySet().iterator();
        Assertions.assertTrue(iterator.hasNext());
        Map.Entry<String, String> next = iterator.next();
        Assertions.assertEquals(url, next.getValue());
    }

    @Test
    public void testHalfUrlCover() {
        String url = "/products?gender=female&tag=x123&tag=x1234&tag=x5678";
        Map<String, String> prettyUrls = service.getPrettyUrls(Collections.singletonList(url));
        Iterator<Map.Entry<String, String>> iterator = prettyUrls.entrySet().iterator();
        Assertions.assertTrue(iterator.hasNext());
        Map.Entry<String, String> next = iterator.next();
        Assertions.assertEquals(url, next.getValue());
    }

}
