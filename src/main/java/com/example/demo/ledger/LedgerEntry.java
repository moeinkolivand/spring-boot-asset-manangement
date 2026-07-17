package com.example.demo.ledger;

import com.example.demo.transaction.Transaction;
import com.example.demo.wallet.Wallet;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "ledger_entries")
public class LedgerEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LedgerEntryDirectionEnum ledgerEntryDirectionEnum;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected LedgerEntry() {}

    public LedgerEntry(Transaction transaction, Wallet wallet,
                       LedgerEntryDirectionEnum ledgerEntryDirectionEnum, BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Ledger entry amount must be positive; direction encodes sign");
        }
        this.transaction = transaction;
        this.wallet = wallet;
        this.ledgerEntryDirectionEnum = ledgerEntryDirectionEnum;
        this.amount = amount;
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public Transaction getTransaction() { return transaction; }
    public Wallet getWallet() { return wallet; }
    public LedgerEntryDirectionEnum getLedgerEntryDirectionEnum() { return ledgerEntryDirectionEnum; }
    public BigDecimal getAmount() { return amount; }
    public Instant getCreatedAt() { return createdAt; }
}