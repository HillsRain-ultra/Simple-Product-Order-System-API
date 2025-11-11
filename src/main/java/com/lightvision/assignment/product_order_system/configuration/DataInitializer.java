package com.lightvision.assignment.product_order_system.configuration;

import com.lightvision.assignment.product_order_system.entity.Product;
import com.lightvision.assignment.product_order_system.entity.User;
import com.lightvision.assignment.product_order_system.enums.ERole;
import com.lightvision.assignment.product_order_system.repository.ProductRepository;
import com.lightvision.assignment.product_order_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting to initialize sample data...");

        // --- 1. CREATE SAMPLE USERS ---

        // Only create if Admin doesn't exist
        if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {
            User admin = new User();
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setName("Admin User");
            admin.setRole(ERole.ROLE_ADMIN);
            admin.setDateOfBirth(LocalDate.of(1990, 1, 1)); // Example DOB

            userRepository.save(admin);
            log.info("Created ADMIN user: admin@gmail.com / admin123");
        }

        // Only create if Customer doesn't exist
        if (userRepository.findByEmail("customer@gmail.com").isEmpty()) {
            User customer = new User();
            customer.setEmail("customer@gmail.com"); // <<< UPDATED
            customer.setPassword(passwordEncoder.encode("customer123"));
            customer.setName("Customer User");
            customer.setRole(ERole.ROLE_CUSTOMER);
            customer.setDateOfBirth(LocalDate.of(1995, 5, 15)); // Example DOB

            userRepository.save(customer);
            log.info("Created CUSTOMER user: customer@gmail.com / customer123");
        }

        // --- 2. CREATE SAMPLE PRODUCTS ---

        if (productRepository.count() == 0) {
            Product p1 = new Product();
            p1.setName("Laptop Pro 15-inch");
            p1.setDescription("High-performance laptop for developers.");
            p1.setPrice(new BigDecimal("1500.00"));
            p1.setStock(50); // In stock
            p1.setCreatedAt(LocalDateTime.now());
            // (You can set p1.setCreator(admin) if your entity relationship is set up)

            Product p2 = new Product();
            p2.setName("Ergonomic Wireless Mouse");
            p2.setDescription("Comfortable ergonomic wireless mouse.");
            p2.setPrice(new BigDecimal("75.50"));
            p2.setStock(5); // Low stock (for testing)
            p2.setCreatedAt(LocalDateTime.now());

            Product p3 = new Product();
            p3.setName("34-inch UltraWide Monitor");
            p3.setDescription("Curved 21:9 aspect ratio monitor.");
            p3.setPrice(new BigDecimal("890.00"));
            p3.setStock(0); // Out of stock (for testing errors)
            p3.setCreatedAt(LocalDateTime.now());

            productRepository.saveAll(List.of(p1, p2, p3));
            log.info("Created 3 sample products.");
        }

        log.info("Sample data initialization complete.");
    }
}
