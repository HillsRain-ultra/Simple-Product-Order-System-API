package com.lightvision.assignment.product_order_system.entity;

import com.lightvision.assignment.product_order_system.enums.ERole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(unique = true, nullable = false)
    String email;

    @Column(nullable = false)
    String password;

    String name;
    LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    ERole role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    List<Order> orders;

    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
    List<Product> createdProducts;

    @OneToMany(mappedBy = "updater", fetch = FetchType.LAZY)
    List<Product> updatedProducts;

}
