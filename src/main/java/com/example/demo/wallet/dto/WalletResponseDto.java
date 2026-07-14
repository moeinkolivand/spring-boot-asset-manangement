package com.example.demo.wallet.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record WalletResponseDto(
        String name,
        Long id,
        BigDecimal balance,
        LocalDate createdAt
) {}
