package com.lightvision.assignment.product_order_system.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class OrderItemRequestDTO {

    @Schema(description = "The unique ID of the product to purchase. (Hint: Get this from 'GET /api/products')",
            example = "9c8a6583-540d-407a-903d-026bb5898380") // <<< (2) GUIDANCE
    @NotEmpty
    String productId;

    @Schema(description = "The number of units to purchase.",
            example = "1")
    int quantity;
}
