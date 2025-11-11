package com.lightvision.assignment.product_order_system.dto.response;

import com.lightvision.assignment.product_order_system.enums.ERole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserCreationResponseDTO {
    String id;
    String email;
    String name;
    LocalDate dateOfBirth;
    ERole role;
}
