package com.stylight.seo;

import com.stylight.seo.domain.UrlService;
import com.stylight.seo.repository.InMemoryRepository;
import com.stylight.seo.service.InMemoryUrlService;
import com.stylight.seo.service.TreeUrlService;
import com.stylight.seo.service.UrlValidationService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ServiceTestConfiguration {

    @Bean
    public TreeUrlService treeUrlService() {
        return new TreeUrlService(new UrlValidationService());
    }

    @Bean
    public UrlService urlService(InMemoryRepository service, TreeUrlService treeUrlService) {
        return new InMemoryUrlService(service, treeUrlService);
    }

    @Bean
    public InMemoryRepository repository() {
        return new InMemoryRepository();
    }
}
