package com.github.dhiegorp.msscbreweryclient.web.client;

import com.github.dhiegorp.msscbreweryclient.web.model.BeerDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BreweryClientTest {

    @Autowired
    BreweryClient breweryClient;

    @Test
    void getBeerbyId() {
        BeerDto dto = breweryClient.getBeerById(UUID.randomUUID());
        assertNotNull(dto);
    }

    @Test
    void saveNewBeer() {
        BeerDto dto = BeerDto.builder().beerName("A new beer").build();
        URI uriNewBeer = breweryClient.saveNewBeer(dto);
        assertNotNull(uriNewBeer);
    }

    @Test
    void updateBeer() {
        //given
        BeerDto dto = BeerDto.builder().beerName("japones").build();
        breweryClient.updateBeer(UUID.randomUUID(), dto);

    }

    @Test
    void deleteBeer() {
        breweryClient.deleteBeer(UUID.randomUUID());
    }
}