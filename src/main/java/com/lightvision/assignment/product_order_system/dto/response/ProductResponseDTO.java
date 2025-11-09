package com.lightvision.assignment.product_order_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ProductResponseDTO {
    String id;
    String name;
    String description;
    BigDecimal price;
    Integer stock;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
