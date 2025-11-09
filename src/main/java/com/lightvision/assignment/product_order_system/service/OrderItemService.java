package com.lightvision.assignment.product_order_system.service;

import com.lightvision.assignment.product_order_system.entity.Order;
import com.lightvision.assignment.product_order_system.entity.OrderItem;
import com.lightvision.assignment.product_order_system.entity.Product;
import com.lightvision.assignment.product_order_system.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final ProductService productService;

    public OrderItem buildAndValidateOrderItem(Order order, Product product, Integer quantity) {

        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("insufficient stock for the product: " + product.getName());
        }

        product.setStock(product.getStock() - quantity);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setPricePerUnit(product.getPrice());

        BigDecimal itemTotalPrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));
        orderItem.setTotalPrice(itemTotalPrice);

        return orderItem;
    }
}
