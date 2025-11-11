package com.lightvision.assignment.product_order_system.service;

import com.lightvision.assignment.product_order_system.dto.request.ProductRequestDTO;
import com.lightvision.assignment.product_order_system.dto.response.ProductResponseDTO;
import com.lightvision.assignment.product_order_system.entity.Product;
import com.lightvision.assignment.product_order_system.exception.ResourceNotFoundException;
import com.lightvision.assignment.product_order_system.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final UserService userService;

    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO, String userId) {
        Product product = new Product();

        product.setName(productRequestDTO.getName());
        product.setDescription(productRequestDTO.getDescription());
        product.setPrice(productRequestDTO.getPrice());
        product.setStock(productRequestDTO.getStock());
        product.setCreatedAt(LocalDateTime.now());
        product.setCreator(userService.findById(userId));

        productRepository.save(product);

        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .createdAt(product.getCreatedAt())
                .build();
    }

    public List<ProductResponseDTO> getProduct() {
        return productRepository.findAll().stream()
                .map(product -> ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build()).toList();
    }

    public ProductResponseDTO getProductById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    public void deleteProductById(String id) {
        // Implementation here
        productRepository.deleteById(id);
    }


    public Product findAndLockProductById(String id) {
        return productRepository.findAndLockById(id)
                .orElseThrow(() -> new RuntimeException("Product not found or cannot be locked: " + id));
    }
}
