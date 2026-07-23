package com.example.demo.ledger;

import com.example.demo.transaction.Transaction;
import com.example.demo.wallet.Wallet;

import java.math.BigDecimal;

public interface LedgerApi {
    void createDebit(Transaction transaction, Wallet wallet, BigDecimal amount);
    void createCredit(Transaction transaction, Wallet wallet,  BigDecimal amount);
}
