package com.lightvision.assignment.product_order_system.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
            @JoinColumn(name="order_id")
    @JsonBackReference
    Order order;
    @ManyToOne(fetch = FetchType.LAZY)
            @JoinColumn(name="product_id")
    Product product;

    int quantity;
    BigDecimal pricePerUnit;
    BigDecimal totalPrice;
}
