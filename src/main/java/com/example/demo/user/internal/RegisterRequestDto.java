package com.example.demo.user.internal;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDto(
        @NotBlank(message = "phone number is required")
        String phoneNumber,

        @NotBlank(message = "password is required")
        String password
) {}
