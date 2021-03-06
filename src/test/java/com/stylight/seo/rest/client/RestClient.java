package com.stylight.seo.rest.client;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.glassfish.jersey.apache.connector.ApacheClientProperties;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.proxy.WebResourceFactory;
import org.springframework.util.CollectionUtils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.ArrayList;
import java.util.List;

public class RestClient implements AutoCloseable {

    private final String endpoint;
    private final PoolingHttpClientConnectionManager connectionManager;
    private final ClientConfig configuration;
    private final List<Object> components = new ArrayList<>();
    private final List<Class<?>> classes = new ArrayList<>();
    private Client client;
    private WebTarget target;
    private ClientBuilder builder;

    public RestClient(String endpoint) {
        configuration = new ClientConfig();
        connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(100);
        connectionManager.setMaxTotal(100);
        configuration.property(ApacheClientProperties.CONNECTION_MANAGER, connectionManager);
        configuration.property(ApacheClientProperties.DISABLE_COOKIES, true);
        configuration.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
        configuration.connectorProvider(new ApacheConnectorProvider());
        this.endpoint = endpoint;
    }

    public RestClient build() {
        this.builder = ClientBuilder.newBuilder().withConfig(configuration);
        if (!CollectionUtils.isEmpty(classes)) {
            classes.forEach(c -> builder.register(c));
        }
        if (!CollectionUtils.isEmpty(components)) {
            components.forEach(c -> builder.register(c));
        }
        client = builder.build();
        target = client.target(endpoint);
        return this;
    }

    public <T> T create(Class<T> clazz) {
        return WebResourceFactory.newResource(clazz, target);
    }

    public RestClient register(Object obj) {
        components.add(obj);
        return this;
    }

    @Override
    public void close() {
        closeConnectionProvider();
        closeClient();
    }

    private void closeClient() {
        try {
            if (client != null) {
                client.close();
            }
        } catch (Exception ignored) {
        }
    }

    private void closeConnectionProvider() {
        try {
            if (connectionManager != null) {
                connectionManager.close();
            }
        } catch (Exception ignored) {
        }
    }

}