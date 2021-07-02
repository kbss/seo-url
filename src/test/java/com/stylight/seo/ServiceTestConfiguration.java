package com.stylight.seo;

import com.stylight.seo.domain.UrlService;
import com.stylight.seo.repository.DBStub;
import com.stylight.seo.service.InMemoryUrlService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ServiceTestConfiguration {

    @Bean
    public DBStub repository() {
        return new DBStub();
    }

    @Bean
    public UrlService urlService(DBStub service) {
        return new InMemoryUrlService(service);
    }


}
