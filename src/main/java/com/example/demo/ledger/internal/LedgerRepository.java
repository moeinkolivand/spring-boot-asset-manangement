package com.example.demo.ledger.internal;

import com.example.demo.ledger.LedgerEntry;
import com.example.demo.transaction.Transaction;
import com.example.demo.wallet.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface LedgerRepository extends JpaRepository<LedgerEntry, Long> { }
