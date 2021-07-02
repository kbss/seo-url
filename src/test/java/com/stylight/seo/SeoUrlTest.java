package com.stylight.seo;

import com.stylight.seo.domain.UrlService;
import com.stylight.seo.service.UrlUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
@Import(ServiceTestConfiguration.class)
public class SeoUrlTest {

    @Autowired
    private UrlService service;

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
