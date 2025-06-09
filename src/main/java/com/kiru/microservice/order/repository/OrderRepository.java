package com.kiru.microservice.order.repository;

import com.kiru.microservice.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
