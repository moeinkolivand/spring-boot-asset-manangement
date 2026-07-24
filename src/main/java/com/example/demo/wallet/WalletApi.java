package com.example.demo.wallet;

import com.example.demo.currency.Currency;

import java.math.BigDecimal;
import java.util.Optional;

public interface WalletApi {
    Optional<Wallet> findByIdForUpdate(Long id);
    Optional<Wallet> findByCurrencyNameAndUser(String currencyName, Long userId);
    void createWallet(Wallet wallet);
    boolean isWalletFrozen(Wallet wallet);
    void createWallet(String name, BigDecimal balance, Long userId, Currency currency);
}
