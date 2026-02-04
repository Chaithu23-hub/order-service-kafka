//package com.MicroService.OrderService.kafka;
//
//import com.MicroService.OrderService.event.OrderCreatedEvent;
//import com.MicroService.OrderService.event.OrderFailedEvent;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//@Service
//public class InventoryResponseConsumer {
//
//    private final OrderEventProducer producer;
//
//    public InventoryResponseConsumer(OrderEventProducer producer) {
//        this.producer = producer;
//    }
//
//    @KafkaListener(topics = "inventory-reserved", groupId = "order-group-v9")
//    public void handleInventoryReserved(OrderCreatedEvent event) {
//        System.out.println("Inventory reserved for order " + event.getOrderId());
//    }
//
//    @KafkaListener(topics = "inventory-failed", groupId = "order-group-v9")
//    public void handleInventoryFailed(OrderCreatedEvent event) {
//        System.out.println("Inventory failed for order " + event.getOrderId());
//    }
//}
//
