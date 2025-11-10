package com.lightvision.assignment.product_order_system.dto.request;

import com.lightvision.assignment.product_order_system.enums.ERole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationRequestDTO {
    @Schema(description = "The user's unique email", example = "customer@example.com")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(description = "The user's password (min 8 characters)", example = "customer123")
    @Size(min = 3, message = "Password must be at least 3 characters long")
    private String password;

    @Schema(description = "The user's full name", example = "Customer User")
    private String name;

    @Schema(description = "Date of birth (YYYY-MM-DD)", example = "2000-11-10")
    private LocalDate dateOfBirth;

    @Schema(description = "Role (ROLE_ADMIN or ROLE_CUSTOMER)", example = "ROLE_CUSTOMER")
    private ERole role;
}
