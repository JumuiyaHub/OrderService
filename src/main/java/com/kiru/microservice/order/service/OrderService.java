package com.kiru.microservice.order.service;

import com.kiru.microservice.order.client.InventoryClient;
import com.kiru.microservice.order.dto.OrderRequest;
import com.kiru.microservice.order.model.Order;
import com.kiru.microservice.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    public void placeOrder(OrderRequest orderRequest) {
        var isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());

        if (isProductInStock) {
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setPrice(orderRequest.price());
            order.setSkuCode(orderRequest.skuCode());
            order.setQuantity(orderRequest.quantity());

            // Save Order to the OrderRepository
            orderRepository.save(order);
        } else {
            throw new RuntimeException("Product with SKU Code " + orderRequest.skuCode() + " is not in stock.");
        }




    }
}
