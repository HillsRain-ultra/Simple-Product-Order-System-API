package com.lightvision.assignment.product_order_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class OrderItemResponseDTO {
    String productId;
    String productName;

    int quantity;
    BigDecimal pricePerUnit;
    BigDecimal totalPrice;
}
