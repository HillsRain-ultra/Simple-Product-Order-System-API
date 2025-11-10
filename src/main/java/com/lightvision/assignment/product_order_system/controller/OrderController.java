package com.lightvision.assignment.product_order_system.controller;

import com.lightvision.assignment.product_order_system.dto.request.OrderRequestDTO;
import com.lightvision.assignment.product_order_system.dto.response.OrderResponseDTO;
import com.lightvision.assignment.product_order_system.service.OrderService;
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
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "3. Order Management", description = "APIs for creating and retrieving orders (Requires authentication)")
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Create a new order",
            description = "Creates a new order, validates stock, and deducts product stock. Requires ADMIN or CUSTOMER role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request (e.g., insufficient stock, invalid quantity)"),
            @ApiResponse(responseCode = "401", description = "Unauthorized (Invalid or missing token)"),
            @ApiResponse(responseCode = "404", description = "Not Found (One or more Product IDs do not exist)")
    })
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO orderRequestDTO, Authentication authentication) {
        String userId = authentication.getName();

        OrderResponseDTO orderResponseDTO = orderService.createOrder(orderRequestDTO, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDTO);
    }

    @Operation(summary = "Get all orders", description = "Retrieves a list of all orders.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "401", description = "Unauthorized (Invalid or missing token)")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }

    @Operation(summary = "Get my orders", description = "Retrieves a list of all orders placed by the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the user's order list"),
            @ApiResponse(responseCode = "401", description = "Unauthorized (Invalid or missing token)")
    })
    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponseDTO>> getMyOrders(Authentication authentication) {
        String userId = authentication.getName();

        List<OrderResponseDTO> orders = orderService.getMyOrders(userId);
        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }

    @Operation(summary = "Get order by ID", description = "Retrieves the details of a specific order by its ID. Requires authentication.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved order details"),
            @ApiResponse(responseCode = "401", description = "Unauthorized (Invalid or missing token)"),
            @ApiResponse(responseCode = "404", description = "Not Found (Order with this ID does not exist)")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable String id) {
        OrderResponseDTO orderResponseDTO = orderService.getOrderById(id);
        return ResponseEntity.ok(orderResponseDTO);
    }
}
