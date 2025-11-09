package com.lightvision.assignment.product_order_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    int stock;
    BigDecimal price;

    String name;
    String description;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    List<OrderItem> orderItem;
}
