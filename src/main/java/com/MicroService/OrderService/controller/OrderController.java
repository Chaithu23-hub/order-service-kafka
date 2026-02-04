package com.MicroService.OrderService.controller;

import com.MicroService.OrderService.dto.OrderRequestDto;
import com.MicroService.OrderService.service.OrderCreationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderCreationService orderCreationService;

    public OrderController(OrderCreationService orderCreationService) {
        this.orderCreationService = orderCreationService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createOrder(
            @RequestBody OrderRequestDto request) {

        orderCreationService.placeOrder(request);

        return ResponseEntity
                .ok("Order received and processing started");
    }
}


