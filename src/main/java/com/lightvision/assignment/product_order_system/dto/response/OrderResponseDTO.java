package com.lightvision.assignment.product_order_system.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class OrderResponseDTO {

    String id;
    LocalDateTime orderDate;
    BigDecimal totalAmount;

    List<OrderItemResponseDTO> items;
}
