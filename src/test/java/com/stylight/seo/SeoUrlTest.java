package com.stylight.seo;

import com.stylight.seo.domain.UrlService;
import com.stylight.seo.service.UrlUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

@SpringBootTest
public class SeoUrlTest {

    @Autowired
    private UrlService service;

    //TODO: implement
    @Test
    public void testUrl() {
        service.getPrettyUrls(Collections.singletonList("/products"));
        service.getPrettyUrls(Collections.singletonList("/products?gender=leamfe&tag=123&tag=1234&tag=5678"));
    }

    //TODO: implement
    @Test
    public void split() {
        List<String> strings = UrlUtils.splitParametrizedUrl("/products?gender=leamfe&tag=123&tag=1234&tag=5678");
    }

}
