package com.MicroService.OrderService.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component

public class OrderRequestDto {
    // Helper DTO class for the request body
        public String productId;
        public int quantity;
}
