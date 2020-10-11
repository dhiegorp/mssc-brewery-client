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
@Component
public class NoBlockingRestTemplateCustomizer implements RestTemplateCustomizer {

    public ClientHttpRequestFactory clientHttpRequestFactory() throws IOReactorException {
        final DefaultConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(IOReactorConfig.custom().
                setConnectTimeout(3000).
                setIoThreadCount(4).
                setSoTimeout(3000).
                build());
        final PoolingNHttpClientConnectionManager manager = new PoolingNHttpClientConnectionManager(ioReactor);
        manager.setDefaultMaxPerRoute(100);
        manager.setMaxTotal(1000);

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
