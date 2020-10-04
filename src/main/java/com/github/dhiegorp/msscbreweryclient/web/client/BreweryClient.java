package com.github.dhiegorp.msscbreweryclient.web.client;

import com.github.dhiegorp.msscbreweryclient.web.model.BeerDto;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.UUID;

@Component
public class BreweryClient {

    public final String BEER_PATH_V1 = "/api/v1/beer/";
    private final APIInfo apiInfo;
    private final RestTemplate restTemplate;

    public BreweryClient(APIInfo apiInfo, RestTemplateBuilder restTemplateBuilder) {
        this.apiInfo = apiInfo;
        this.restTemplate = restTemplateBuilder.build();
    }


    public BeerDto getBeerById(UUID id) {
        return restTemplate.getForObject(apiInfo.getHost() + BEER_PATH_V1 + id.toString(), BeerDto.class);
    }

    public URI saveNewBeer(BeerDto beer) {
        return restTemplate.postForLocation(apiInfo.getHost()  + BEER_PATH_V1, beer);
    }

    public void updateBeer(UUID id, BeerDto beer) {
        restTemplate.put(apiInfo.getHost() + BEER_PATH_V1 + id.toString(), beer);
    }

    public void  deleteBeer(UUID id) {
        restTemplate.delete(apiInfo.getHost() + BEER_PATH_V1  +id.toString());
    }
}
