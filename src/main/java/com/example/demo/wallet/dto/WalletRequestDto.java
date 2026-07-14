package com.example.demo.wallet.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record WalletRequestDto(
        @NotNull
        @Size(min = 3, max = 20, message = "name must be between 2 and 100 characters")
        String name,

        @NotNull(message = "balance cannot be null")
        @Positive(message = "balance must be positive")
        BigDecimal balance
) {}
