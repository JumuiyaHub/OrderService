package com.kiru.microservice.order.service;

import com.kiru.microservice.order.client.InventoryClient;
import com.kiru.microservice.order.dto.OrderRequest;
import com.kiru.microservice.order.event.OrderPlacedEvent;
import com.kiru.microservice.order.model.Order;
import com.kiru.microservice.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.kiru.microservice.order.client.InventoryClient.log;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public void placeOrder(OrderRequest orderRequest) {
        // Validate OrderRequest
        if (orderRequest == null || orderRequest.skuCode() == null || orderRequest.quantity() == null) {
            throw new IllegalArgumentException("Invalid OrderRequest: SKU Code and Quantity must not be null.");
        }

        // Check product availability
        var isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());

        if (isProductInStock) {
            // Create and save the order
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setPrice(orderRequest.price());
            order.setSkuCode(orderRequest.skuCode());
            order.setQuantity(orderRequest.quantity());
            orderRepository.save(order);

            // Send event to Kafka
            OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent();
            orderPlacedEvent.setOrderNumber(order.getOrderNumber());
            orderPlacedEvent.setEmail(orderRequest.userDetails().email());
            orderPlacedEvent.setFirstName(orderRequest.userDetails().firstName());
            orderPlacedEvent.setLastName(orderRequest.userDetails().lastName());
            log.info("Start - Sending OrderPlacedEvent {} to Kafka topic order-placed", orderPlacedEvent);
            kafkaTemplate.send("order-placed-topic", orderPlacedEvent);
            log.info("End - Sending OrderPlacedEvent {} to Kafka topic order-placed", orderPlacedEvent);
        } else {
            throw new RuntimeException("Product with SKU Code " + orderRequest.skuCode() + " is not in stock.");
        }
    }
}