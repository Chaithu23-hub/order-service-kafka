package com.MicroService.OrderService.controller;

import com.MicroService.OrderService.dto.OrderRequestDto;
import com.MicroService.OrderService.service.OrderCreationService;
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

    // This is the API endpoint you hit from Postman
    @PostMapping("/create")
    public String createOrder(@RequestBody OrderRequestDto orderRequest) {
        return orderCreationService.placeOrder(
                orderRequest.getProductId(),
                orderRequest.getQuantity()
        );
    }
}

