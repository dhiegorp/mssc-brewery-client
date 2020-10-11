package com.github.dhiegorp.msscbreweryclient.web.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.IOReactorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * There can be only one! If you want to use the non-blocking version of http client configured to work within the {@link org.springframework.web.client.RestTemplate},
 * annotate this class as @Component and remove the same annotation from the {@link BlockingRestTemplateCustomizer}.
 */
@Slf4j
//@Component
public class NoBlockingRestTemplateCustomizer implements RestTemplateCustomizer {

    private final Integer connectionTimeout;
    private final Integer ioThreadCount;
    private final Integer soTimeout;
    private final Integer defaultMaxConnectionsPerRoute;
    private final Integer defaultMaxConnections;

    public NoBlockingRestTemplateCustomizer(@Value("${resttemplate.nonblocking.connectiontimeout}") Integer connectionTimeout,
                                            @Value("${resttemplate.nonblocking.iothreadcount}") Integer ioThreadCount,
                                            @Value("${resttemplate.nonblocking.sotimeout}") Integer soTimeout,
                                            @Value("${resttemplate.nonblocking.defaultmaxconnectionsperroute}") Integer defaultMaxConnectionsPerRoute,
                                            @Value("${resttemplate.nonblocking.defaultmaxconnections}") Integer defaultMaxConnections) {
        this.connectionTimeout = connectionTimeout;
        this.ioThreadCount = ioThreadCount;
        this.soTimeout = soTimeout;
        this.defaultMaxConnectionsPerRoute = defaultMaxConnectionsPerRoute;
        this.defaultMaxConnections = defaultMaxConnections;
    }

    public ClientHttpRequestFactory clientHttpRequestFactory() throws IOReactorException {
        final DefaultConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(IOReactorConfig.custom().
                setConnectTimeout(connectionTimeout).
                setIoThreadCount(ioThreadCount).
                setSoTimeout(soTimeout).
                build());
        final PoolingNHttpClientConnectionManager manager = new PoolingNHttpClientConnectionManager(ioReactor);
        manager.setDefaultMaxPerRoute(defaultMaxConnectionsPerRoute);
        manager.setMaxTotal(defaultMaxConnections);

        CloseableHttpAsyncClient asyncClient = HttpAsyncClients.custom().
                setConnectionManager(manager).
                build();

        return new HttpComponentsAsyncClientHttpRequestFactory(asyncClient); //deprecated in Spring 5


    }

    @Override
    public void customize(RestTemplate restTemplate) {
        try {
            log.info("Using Non Blocking HTTP Client");
            restTemplate.setRequestFactory(clientHttpRequestFactory());
        } catch(IOReactorException e) {
            log.error("an error occurred while setting up the RestTemplate HTTP client! " , e);
        }
    }
}
