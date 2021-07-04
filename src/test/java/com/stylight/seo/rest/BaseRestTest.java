package com.stylight.seo.rest;

import com.stylight.seo.SeoUrlApplication;
import com.stylight.seo.rest.client.RestClient;
import com.stylight.seo.rest.resource.UrlTestResource;
import org.glassfish.jersey.logging.LoggingFeature;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.logging.Level;

@SpringBootTest(classes = {SeoUrlApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseRestTest {
    @LocalServerPort
    int randomServerPort = 8080;

    private RestClient client;
    private UrlTestResource resource;

    private RestClient getClient() {
        if (client == null) {
            client = new RestClient(getBaseUrl())
                    .register(buildLoggingFeature())
                    .build();
        }
        return client;
    }

    private LoggingFeature buildLoggingFeature() {
        return new LoggingFeature(java.util.logging.Logger.getLogger(RestPrettyUrlTests.class.getName()), Level.INFO, null, null);
    }

    public UrlTestResource getResource() {
        if (resource == null) {
            resource = getClient().create(UrlTestResource.class);
        }
        return resource;
    }

    private String getBaseUrl() {
        return "http://localhost:" + randomServerPort;
    }
}