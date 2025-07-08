package com.kiru.microservice.order.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record OrderRequest(
        Long id,
        String skuCode,
        Integer quantity,
        String orderNumber,
//        @NotNull(message = "SKU Code must not be null") String skuCode,
        BigDecimal price,
//        @NotNull(message = "Quantity must not be null") Integer quantity,
        UserDetails userDetails) {

    public record UserDetails(String email, String firstName, String lastName) {
    }
}