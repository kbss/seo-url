package com.stylight.seo.application;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.util.Objects;


@EnableCaching
@Configuration
public class CacheConfiguration {

    public static final String PARAMETRIZED_URL = "parametrizedUrl";
    public static final String PRETTY_URL = "prettyUrl";

    @Bean
    public CacheManager cacheManager() {
        return new EhCacheCacheManager(Objects.requireNonNull(ehCacheCacheManager().getObject()));
    }

    @Bean
    public EhCacheManagerFactoryBean ehCacheCacheManager() {
        EhCacheManagerFactoryBean factory = new EhCacheManagerFactoryBean();
        factory.setConfigLocation(new ClassPathResource("config/ehcache.xml"));
        factory.setShared(true);
        return factory;
    }

}