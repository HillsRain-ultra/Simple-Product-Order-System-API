package com.lightvision.assignment.product_order_system.controller;

import com.lightvision.assignment.product_order_system.dto.request.OrderRequestDTO;
import com.lightvision.assignment.product_order_system.dto.response.OrderResponseDTO;
import com.lightvision.assignment.product_order_system.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        OrderResponseDTO orderResponseDTO = orderService.createOrder(orderRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable String id) {
        OrderResponseDTO orderResponseDTO = orderService.getOrderById(id);
        return ResponseEntity.ok(orderResponseDTO);
    }
}
