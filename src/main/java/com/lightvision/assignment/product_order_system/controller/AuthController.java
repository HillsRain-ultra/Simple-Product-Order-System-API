package com.lightvision.assignment.product_order_system.controller;

import com.lightvision.assignment.product_order_system.dto.request.AuthenticationRequestDTO;
import com.lightvision.assignment.product_order_system.dto.request.UserCreationRequestDTO;
import com.lightvision.assignment.product_order_system.dto.response.AuthenticationResponseDTO;
import com.lightvision.assignment.product_order_system.dto.response.UserCreationResponseDTO;
import com.lightvision.assignment.product_order_system.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping ("/api/auth")
@Tag(name = "1. Authentication Service", description = "APIs for User Sign Up and Sign In")
public class AuthController {
    private final AuthService userService;

    @Operation(summary = "Register a new user", description = "Creates a new user account and returns a JWT token.") // <<< (B)
    @ApiResponses(value = { // <<< (C)
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request (e.g., email already exists)")
    })
    @PostMapping("/create")
    public ResponseEntity<UserCreationResponseDTO> createUser(@RequestBody @Valid UserCreationRequestDTO request) {
        UserCreationResponseDTO response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Sign in a user", description = "Authenticates a user and returns a JWT token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful, token provided"),
            @ApiResponse(responseCode = "401", description = "Unauthorized (Invalid email or password)")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> login(@RequestBody AuthenticationRequestDTO request) {
        try{
            AuthenticationResponseDTO response = userService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
