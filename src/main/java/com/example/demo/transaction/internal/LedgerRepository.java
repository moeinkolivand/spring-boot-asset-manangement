package com.example.demo.transaction.internal;

import com.example.demo.transaction.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LedgerRepository extends JpaRepository<LedgerEntry, Long> { }
