package com.lightvision.assignment.product_order_system.service;

import com.lightvision.assignment.product_order_system.dto.request.OrderItemRequestDTO;
import com.lightvision.assignment.product_order_system.dto.request.OrderRequestDTO;
import com.lightvision.assignment.product_order_system.dto.response.OrderItemResponseDTO;
import com.lightvision.assignment.product_order_system.dto.response.OrderResponseDTO;
import com.lightvision.assignment.product_order_system.entity.Order;
import com.lightvision.assignment.product_order_system.entity.OrderItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.lightvision.assignment.product_order_system.entity.Product;
import com.lightvision.assignment.product_order_system.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;
    private final ProductService productService;

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {

        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());


        List<OrderItem> builtOrderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for(OrderItemRequestDTO itemDTO : orderRequestDTO.getItems()) {
            Product lockedProduct = productService.findAndLockProductById(itemDTO.getProductId());

            OrderItem orderItem = orderItemService.buildAndValidateOrderItem(
                    order,
                    lockedProduct,
                    itemDTO.getQuantity()
            );

            builtOrderItems.add(orderItem);
            totalAmount = totalAmount.add(orderItem.getTotalPrice());
        }

        order.setTotalAmount(totalAmount);
        order.setOrderItems(builtOrderItems);

        Order savedOrder = orderRepository.save(order);

        return mapToResponseDTO(savedOrder);
    }

    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    public OrderResponseDTO getOrderById(String id) {
        return mapToResponseDTO(orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found")));
    }

    private OrderResponseDTO mapToResponseDTO(Order order) {
        return OrderResponseDTO.builder()
                .id(order.getId())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .items(order.getOrderItems().stream().map(item -> OrderItemResponseDTO.builder()
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .pricePerUnit(item.getPricePerUnit())
                        .totalPrice(item.getTotalPrice())
                        .build()).collect(Collectors.toList()))
                .build();
    }
}
