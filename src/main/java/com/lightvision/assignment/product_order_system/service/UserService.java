package com.lightvision.assignment.product_order_system.service;

import com.lightvision.assignment.product_order_system.entity.User;
import com.lightvision.assignment.product_order_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findById(String id) {
        return userRepository.findById(id).orElse(null);
    }
}
