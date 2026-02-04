package com.MicroService.OrderService.kafka;

import com.MicroService.OrderService.dto.OrderStatus;
import com.MicroService.OrderService.event.OrderEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishOrderCreated(String orderId, String productId, int qty) {
        kafkaTemplate.send("order-events", new OrderEvent(orderId, productId, qty, OrderStatus.CREATED));
    }

    public void publishOrderFailed(String orderId, String productId, int qty) {
        kafkaTemplate.send("order-events", new OrderEvent(orderId, productId, qty, OrderStatus.FAILED));
    }
}
