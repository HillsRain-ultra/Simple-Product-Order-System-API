package com.lightvision.assignment.product_order_system.repository;

import com.lightvision.assignment.product_order_system.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, String> {

}
