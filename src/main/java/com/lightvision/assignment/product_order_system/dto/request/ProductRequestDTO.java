package com.lightvision.assignment.product_order_system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {

    @Schema(description = "The name of the product.",
            example = "Pro Laptop 15-inch")
    @NotEmpty
    private String name;

    @Schema(description = "A brief description of the product.",
            example = "A high-performance laptop for professionals.")
    private String description;

    @Schema(description = "The selling price of the product.",
            example = "1499.99")
    @NotNull
    @DecimalMin("0.0")
    private BigDecimal price;

    @Schema(description = "The initial stock quantity.",
            example = "50")
    @NotNull
    private Integer stock;
}
