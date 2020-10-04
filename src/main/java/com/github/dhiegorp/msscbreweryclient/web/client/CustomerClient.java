package com.github.dhiegorp.msscbreweryclient.web.client;

import com.github.dhiegorp.msscbreweryclient.web.model.CustomerDto;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.UUID;

@Component
public class CustomerClient {

    public final String CUSTOMER_V1_API = "/api/v1/customer/";
    private final APIInfo info;
    private final RestTemplate restTemplate;


    public CustomerClient(RestTemplateBuilder builder, APIInfo info) {
        this.info = info;
        this.restTemplate = builder.build();
    }

    public CustomerDto getCustomerById(UUID id) {
        return restTemplate.getForObject(info.getHost() + CUSTOMER_V1_API + id.toString(), CustomerDto.class);
    }


    public URI saveNewCustomer(CustomerDto dto) {
        return restTemplate.postForLocation(info.getHost() + CUSTOMER_V1_API, dto);
    }

    public void updateCustomer(UUID toUpdate, CustomerDto dto) {
        restTemplate.put(info.getHost() + CUSTOMER_V1_API + toUpdate, dto);
    }

    public void deleteById(UUID toDelete) {
        restTemplate.delete(info.getHost() + CUSTOMER_V1_API + toDelete);
    }
}
