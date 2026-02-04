package com.MicroService.OrderService.event;

import com.MicroService.OrderService.dto.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {
    private String orderId;
    private String productId;
    private int quantity;
    private OrderStatus status;
}
