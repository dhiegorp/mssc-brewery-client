package com.github.dhiegorp.msscbreweryclient.web.client;

import com.github.dhiegorp.msscbreweryclient.web.model.CustomerDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerClientTest {

    @Autowired
    CustomerClient client;

    @Test
    void getCustomerById() {
        CustomerDto dto = client.getCustomerById(UUID.randomUUID());
        assertNotNull(dto);

    }

    @Test
    void saveNewCustomer() {
        CustomerDto dto = CustomerDto.builder().name("New Customer").build();
        URI locationNewCustomer = client.saveNewCustomer(dto);
    }

    @Test
    void updateCustomer() {
        CustomerDto dto = CustomerDto.builder().name("A new customer").build();
        UUID toUpdate = UUID.randomUUID();
        client.updateCustomer(toUpdate, dto);
    }

    @Test
    void deleteCustomer() {
        UUID toDelete = UUID.randomUUID();
        client.deleteById(toDelete);
    }



}