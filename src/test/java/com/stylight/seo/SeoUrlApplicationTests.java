package com.stylight.seo;

import com.stylight.seo.domain.UrlRepository;
import com.stylight.seo.domain.UrlService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SeoUrlApplicationTests {

    @Autowired
    private UrlService urlService;
    @Autowired
    private UrlRepository repository;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(urlService);
        Assertions.assertNotNull(repository);
    }
}
