package com.lightvision.assignment.product_order_system.controller;

import com.lightvision.assignment.product_order_system.dto.request.ProductRequestDTO;
import com.lightvision.assignment.product_order_system.dto.response.ProductResponseDTO;
import com.lightvision.assignment.product_order_system.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "2. Product Management", description = "APIs for creating, retrieving, and deleting products (Requires ADMIN role for modification)")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "Create a new product", description = "Registers a new product in the system. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request (e.g., invalid input data)"),
            @ApiResponse(responseCode = "401", description = "Unauthorized (Invalid or missing token)"),
            @ApiResponse(responseCode = "403", description = "Forbidden (User is not an ADMIN)")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductRequestDTO productRequestDTO, Authentication authentication) {
        String id = authentication.getName();

        ProductResponseDTO createdProduct = productService.createProduct(productRequestDTO, id);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @Operation(summary = "Get all products", description = "Retrieves a list of all available products. Requires authentication.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "401", description = "Unauthorized (Invalid or missing token)")
    })
    @GetMapping()
    public ResponseEntity<List<ProductResponseDTO>> getProducts() {
        List<ProductResponseDTO> products = productService.getProduct();

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @Operation(summary = "Get product by ID", description = "Retrieves the details of a specific product by its ID. Requires authentication.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved product details"),
            @ApiResponse(responseCode = "401", description = "Unauthorized (Invalid or missing token)"),
            @ApiResponse(responseCode = "404", description = "Not Found (Product with this ID does not exist)")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable String id) {
        ProductResponseDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Delete a product", description = "Deletes a product from the system by its ID. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized (Invalid or missing token)"),
            @ApiResponse(responseCode = "403", description = "Forbidden (User is not an ADMIN)"),
            @ApiResponse(responseCode = "404", description = "Not Found (Product with this ID does not exist)")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProductById(@PathVariable String id) {
        productService.deleteProductById(id);

        return ResponseEntity.noContent().build();
    }
}
