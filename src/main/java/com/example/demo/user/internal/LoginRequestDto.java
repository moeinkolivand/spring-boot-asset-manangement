package com.example.demo.user.internal;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank(message = "PhoneNumber is required")
        String phoneNumber,

        @NotBlank(message = "Password is required")
        String password
) {
}