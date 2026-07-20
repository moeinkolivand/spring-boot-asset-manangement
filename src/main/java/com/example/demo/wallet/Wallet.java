package com.example.demo.wallet;

import com.example.demo.currency.Currency;
import com.example.demo.user.User;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(
        name = "wallets",
        indexes = {
                @Index(name = "idx_wallet_name", columnList = "name"),
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_user_currency", columnNames = {"user_id", "currency_id"})
        }
)
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal balance;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @ColumnDefault("'ACTIVE'")
    @Enumerated(EnumType.STRING)
    private WalletStatus walletStatus;


    public Wallet() {
    }

    public Wallet(String name, BigDecimal balance, User user, Currency currency) {
        this.name = name;
        this.balance = balance;
        this.user = user;
        this.currency = currency;
        this.createdAt = Instant.now();
    }

    public Wallet(String name, BigDecimal balance) {
        this.name = name;
        this.balance = balance;
        this.createdAt = Instant.now();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public WalletStatus getWalletStatus() {
        return walletStatus;
    }

    public void setWalletStatus(WalletStatus walletStatus) {
        this.walletStatus = walletStatus;
    }
}
