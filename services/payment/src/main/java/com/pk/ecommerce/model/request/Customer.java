package com.pk.ecommerce.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.validation.annotation.Validated;

@Validated
@Builder
public record Customer(
        String id,

        @NotNull(message = "Customer firstname is required")
        String firstName,

        @NotNull(message = "Customer lastname is required")
        String lastName,

        @NotNull(message = "Customer email is required")
        @Email(message = "Customer email is not valid email address")
        String email
) {
}
