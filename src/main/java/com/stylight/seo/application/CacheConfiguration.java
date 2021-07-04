package com.stylight.seo.application;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;


@EnableCaching
@Configuration
public class CacheConfiguration {
    public static final String PARAMETRIZED_URL = "parametrizedUrl";
    public static final String PRETTY_URL = "prettyUrl";

}