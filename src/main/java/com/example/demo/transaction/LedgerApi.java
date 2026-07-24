package com.example.demo.transaction;

import com.example.demo.wallet.Wallet;

import java.math.BigDecimal;

public interface LedgerApi {
    void createDebit(Transaction transaction, Wallet wallet, BigDecimal amount);
    void createCredit(Transaction transaction, Wallet wallet,  BigDecimal amount);
}
