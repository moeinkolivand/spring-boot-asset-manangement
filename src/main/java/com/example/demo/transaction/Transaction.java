package com.example.demo.transaction;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @Column
    private TransactionType transactionType;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;


    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    public Transaction() {
    }

    public Transaction(TransactionStatus transactionStatus) {
        this.createdAt = Instant.now();
        this.transactionStatus = transactionStatus;
    }


    public Long getId() {
        return id;
    }

    public TransactionStatus getTransactionType() {
        return transactionStatus;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

}
