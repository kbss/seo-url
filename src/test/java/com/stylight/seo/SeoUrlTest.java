package com.stylight.seo;

import com.stylight.seo.repository.DBStub;
import com.stylight.seo.service.InMemoryUrlService;
import com.stylight.seo.service.UrlUtils;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;


public class SeoUrlTest {


    private InMemoryUrlService service = new InMemoryUrlService(new DBStub());


    @Test
    public void testUrl() {

//      Map<String, String> prettyUrls = service.getPrettyUrls(Collections.singletonList("/products?gender=female&tag=123&tag=1234"));
        service.getPrettyUrls(Collections.singletonList("/products"));
        service.getPrettyUrls(Collections.singletonList("/products?gender=leamfe&tag=123&tag=1234&tag=5678"));

    }

    @Test
    public void split() {
        List<String> strings = UrlUtils.splitParametrizedUrl("/products?gender=leamfe&tag=123&tag=1234&tag=5678");
        strings.forEach(System.out::println);
        System.out.println(strings.size());
    }

}
