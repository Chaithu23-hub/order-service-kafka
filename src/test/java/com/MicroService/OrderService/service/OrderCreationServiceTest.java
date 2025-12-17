package com.MicroService.OrderService.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringBootTest // Loads the full Spring context, including the retry mechanism
public class OrderCreationServiceTest {

    @Autowired
    private OrderCreationService orderCreationService;

    @MockBean // Creates a mock version of RestTemplate, replacing the real one
    private RestTemplate restTemplate;

    @Test
    void whenPlaceOrderFails_itShouldRetryThreeTimes() {
        // Arrange: Tell the mock RestTemplate what to do
        when(restTemplate.postForEntity(anyString(), any(), any(Class.class), anyString(), any(Integer.class)))
                .thenThrow(new ResourceAccessException("I/O error 1")) // Fail on the 1st call
                .thenThrow(new ResourceAccessException("I/O error 2")) // Fail on the 2nd call
                .thenReturn(ResponseEntity.ok("Mock success!"));      // Succeed on the 3rd call

        // Act: Call the method we want to test
        orderCreationService.placeOrder("TEST-PRODUCT", 1);

        // Assert: Verify that the postForEntity method was called exactly 3 times.
        Mockito.verify(restTemplate, times(3))
                .postForEntity(anyString(), any(), any(Class.class), anyString(), any(Integer.class));
    }
}