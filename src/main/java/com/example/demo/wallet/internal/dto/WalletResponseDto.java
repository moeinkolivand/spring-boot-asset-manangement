package com.example.demo.wallet.internal.dto;

import com.example.demo.currency.Currency;

import java.math.BigDecimal;
import java.time.Instant;

public record WalletResponseDto(
        String name,
        Long id,
        BigDecimal balance,
        Currency currency,
        Instant createdAt
) {
}
