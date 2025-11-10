package com.lightvision.assignment.product_order_system.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Simple Product & Order System API",
                version = "1.0.0",
                description = "API Specification for the LightVision Assignment"
        ),
        tags = {
                @Tag(name = "1. Authentication Service", description = "APIs for User Sign Up and Sign In"),
                @Tag(name = "2. Product Management", description = "APIs for creating, retrieving, and deleting products (Requires ADMIN role for modification)"),
                @Tag(name = "3. Order Management", description = "APIs for creating and retrieving orders (Requires authentication)")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
}