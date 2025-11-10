package com.lightvision.assignment.product_order_system.service;

import com.lightvision.assignment.product_order_system.dto.request.AuthenticationRequestDTO;
import com.lightvision.assignment.product_order_system.dto.request.UserCreationRequestDTO;
import com.lightvision.assignment.product_order_system.dto.response.AuthenticationResponseDTO;
import com.lightvision.assignment.product_order_system.dto.response.UserCreationResponseDTO;
import com.lightvision.assignment.product_order_system.entity.User;
import com.lightvision.assignment.product_order_system.enums.ERole;
import com.lightvision.assignment.product_order_system.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.experimental.NonFinal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String SIGNER_KEY;

    // (2) SỬ DỤNG CONSTRUCTOR INJECTION (Sửa lỗi @Value của bạn)
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder, // Spring sẽ tự tiêm Bean
                       @Value("${jwt.signerKey}") String signerKey) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.SIGNER_KEY = signerKey;
    }

    public UserCreationResponseDTO createUser(UserCreationRequestDTO request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("User with email " + request.getEmail() + " already exists.");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setDateOfBirth(request.getDateOfBirth());

        if(request.getRole() != null) {
            user.setRole(request.getRole());
        } else {
            user.setRole(ERole.ROLE_CUSTOMER);
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user = userRepository.save(user);
        return UserCreationResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .dateOfBirth(user.getDateOfBirth())
                .role(user.getRole())
                .build();
    }

    public AuthenticationResponseDTO login(AuthenticationRequestDTO request) throws KeyLengthException {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new IllegalArgumentException("Authentication failed.");
        }

        String token = generateJwtToken(user);
        return AuthenticationResponseDTO.builder()
                .token(token)
                .role(user.getRole())
                .build();
    }


    private String generateJwtToken(User user) throws KeyLengthException {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getId())
                .issuer("product-order")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(5, ChronoUnit.HOURS).toEpochMilli()
                )) // 1 hour expiration
                .claim("email", user.getEmail())
                .claim("role", user.getRole().name())
                .build();

        Payload payload = new Payload(claimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try{
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Error signing the JWT token", e);
        }
    }
}
