package com.example.demo.user;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank(message = "PhoneNumber is required")
        String phoneNumber,

        @NotBlank(message = "Password is required")
        String password
) {
}