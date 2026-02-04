package com.MicroService.OrderService.service;

import com.MicroService.OrderService.dto.OrderRequestDto;
import com.MicroService.OrderService.kafka.OrderEventProducer;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class OrderCreationService {
    private final OrderEventProducer orderEventProducer;

    public OrderCreationService(OrderEventProducer orderEventProducer) {
        this.orderEventProducer = orderEventProducer;
    }

    public void placeOrder(OrderRequestDto dto) {

        boolean paymentFailed = false;

        if (paymentFailed) {
            orderEventProducer.publishOrderFailed(
                    UUID.randomUUID().toString(),
                    dto.getProductId(),
                    dto.getQuantity()
            );
        } else {
            orderEventProducer.publishOrderCreated(
                    UUID.randomUUID().toString(),
                    dto.getProductId(),
                    dto.getQuantity()
            );
        }
    }
}

