package com.github.dhiegorp.msscbreweryclient.web.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * There can be only one! If you want to use the non-blocking version of http client configured to work within the {@link org.springframework.web.client.RestTemplate},
 * annotate this class as @Component and remove the same annotation from the {@link NoBlockingRestTemplateCustomizer}.
 */
@Slf4j
//@Component
public class BlockingRestTemplateCustomizer implements RestTemplateCustomizer {

    public final BlockingRestTemplateConfiguration config;


    public BlockingRestTemplateCustomizer(BlockingRestTemplateConfiguration config) {
        this.config = config;
    }

    public ClientHttpRequestFactory clientHttpRequestFactory() {
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        manager.setMaxTotal(config.getMaxConnectionsOnPool());
        manager.setDefaultMaxPerRoute(config.getMaxConnectionsPerRoute());

        RequestConfig requestConfig = RequestConfig
                .custom()
                .setConnectionRequestTimeout(config.getRequestTimeout())
                .setSocketTimeout(config.getRequestSocketTimeout())
                .build();

        CloseableHttpClient client = HttpClients
                .custom()
                .setConnectionManager(manager)
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                .setDefaultRequestConfig(requestConfig)
                .build();
        return new HttpComponentsClientHttpRequestFactory(client);
    }

    @Override
    public void customize(RestTemplate restTemplate) {
        log.info("Using Blocking HTTP Client");
        restTemplate.setRequestFactory(this.clientHttpRequestFactory());
    }
}
