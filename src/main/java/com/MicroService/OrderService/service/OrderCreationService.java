package com.MicroService.OrderService.service;

import com.MicroService.OrderService.dto.OrderRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderCreationService {
    private static final Logger logger = LoggerFactory.getLogger(OrderCreationService.class);

    private final RestTemplate restTemplate;

    public OrderCreationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<String> placeOrdersFromFile(String filePath) {
        List<String> results = new ArrayList<>();
        List<OrderRequestDto> orders = readOrdersFromFile(filePath);

        if (orders.isEmpty()) {
            results.add("No valid orders found in the file.");
            return results;
        }

       logger.info("Found{}",orders.size() + " orders. Starting processing...");

        for (OrderRequestDto order : orders) {
            String result = placeOrder(order.getProductId(), order.getQuantity());
            results.add("Product ID: " + order.getProductId() + ", Quantity: " + order.getQuantity() + " -> " + result);
        }

        logger.info("Batch processing finished.");
        return results;
    }
    private List<OrderRequestDto> readOrdersFromFile(String filePath){
            List<OrderRequestDto>orders =new ArrayList<>();
            try(BufferedReader reader=new BufferedReader(new FileReader(filePath))){
                String line;
                while((line=reader.readLine())!=null){
                    String[] parts=line.split(",");
                    if(parts.length==2){
                        String productId=parts[0].trim();
                        int quantity=Integer.parseInt(parts[1].trim());
                        orders.add(new OrderRequestDto(productId,quantity));
                    }else{
                        logger.error("Skipping malformed line{}",line);
                    }
                }
            }catch (IOException|NumberFormatException e){
                logger.error("Failed to read or parse file:{}",e.getMessage());
            }
            return orders;

        }

        @Retryable(
            value = { ResourceAccessException.class, HttpServerErrorException.class }, // Retry on network errors or 5xx server errors
            maxAttempts = 3, // Try a total of 3 times
            backoff = @Backoff(delay = 2000, multiplier = 2) // Wait 2s, then 4s before retrying
    )
    public String placeOrder(String productId, int quantity) {
        logger.info("Attempting to call Inventory Service for product: {}", productId);
        String inventoryUrl = "http://localhost:8081/inventory/decrease-stock/{productId}?quantityToDecrease={quantity}";

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    inventoryUrl,
                    null,
                    String.class,
                    productId,
                    quantity
            );
            return "SUCCESS: " + response.getBody();
        } catch (HttpClientErrorException ex) {
            return "FAILED: " + ex.getResponseBodyAsString();
        }
    }
    /**
     * This is the fallback method. It's called ONLY after all retry attempts have failed.
     * The parameters must match the @Retryable method, plus the exception that caused the failure.
     */
    @Recover
    public String recoverFromInventoryServiceFailure(Exception ex, String productId, int quantity) {
        logger.error("RECOVERY: All attempts to call Inventory Service failed for product '{}'. Error: {}", productId, ex.getMessage());
        // Return a safe, specific failure message.
        return "FAILED_PERMANENTLY: Could not update inventory for product " + productId + " after multiple attempts.";
    }

    /**
     * We also need a recover method for HttpClientErrorException, even though we don't retry it.
     * This ensures any uncaught client error is gracefully handled.
     */
    @Recover
    public String recoverFromClientError(HttpClientErrorException ex, String productId, int quantity) {
       logger.error("RECOVERY: A client error occurred for product '{}'. No retries were performed. Error:{} " ,productId ,ex.getResponseBodyAsString());
        return "FAILED: " + ex.getResponseBodyAsString();
    }
}

