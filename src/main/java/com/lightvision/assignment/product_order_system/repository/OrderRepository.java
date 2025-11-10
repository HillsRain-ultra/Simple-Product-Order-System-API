package com.lightvision.assignment.product_order_system.repository;

import com.lightvision.assignment.product_order_system.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByUser_Id(String userId);
}
