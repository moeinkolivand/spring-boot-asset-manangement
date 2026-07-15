package com.example.demo.user;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDto(
        @NotBlank(message = "phone number is required")
        String phoneNumber,

        @NotBlank(message = "password is required")
        String password
) {}
