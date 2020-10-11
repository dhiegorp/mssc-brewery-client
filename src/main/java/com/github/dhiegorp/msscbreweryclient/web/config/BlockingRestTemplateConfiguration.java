package com.github.dhiegorp.msscbreweryclient.web.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("resttemplate.blocking")
public class BlockingRestTemplateConfiguration {

    private Integer maxConnectionsPerRoute;
    private Integer requestTimeout;
    private Integer requestSocketTimeout;
    private Integer maxConnectionsOnPool;

    public Integer getMaxConnectionsPerRoute() {
        return maxConnectionsPerRoute;
    }

    public void setMaxConnectionsPerRoute(Integer maxConnectionsPerRoute) {
        this.maxConnectionsPerRoute = maxConnectionsPerRoute;
    }

    public Integer getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(Integer requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public Integer getRequestSocketTimeout() {
        return requestSocketTimeout;
    }

    public void setRequestSocketTimeout(Integer requestSocketTimeout) {
        this.requestSocketTimeout = requestSocketTimeout;
    }

    public Integer getMaxConnectionsOnPool() {
        return maxConnectionsOnPool;
    }

    public void setMaxConnectionsOnPool(Integer maxConnectionsOnPool) {
        this.maxConnectionsOnPool = maxConnectionsOnPool;
    }
}
