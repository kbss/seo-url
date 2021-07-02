package com.stylight.seo;

import com.stylight.seo.domain.UrlService;
import com.stylight.seo.repository.InMemoryRepository;
import com.stylight.seo.service.InMemoryUrlService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ServiceTestConfiguration {

    @Bean
    public InMemoryRepository repository() {
        return new InMemoryRepository();
    }

    @Bean
    public UrlService urlService(InMemoryRepository service) {
        return new InMemoryUrlService(service);
    }


}
