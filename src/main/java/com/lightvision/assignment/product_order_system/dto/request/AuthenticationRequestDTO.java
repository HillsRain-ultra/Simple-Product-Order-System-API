package com.lightvision.assignment.product_order_system.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AuthenticationRequestDTO {

    @Schema(description = "User's email for login.",
            example = "admin@gmail.com") // <<< (2) THÊM VÍ DỤ ADMIN
    @NotEmpty
    @Email
    String email;

    @Schema(description = "User's password for login.",
            example = "admin123") // <<< (3) THÊM VÍ DỤ ADMIN
    @NotEmpty
    String password;
}
