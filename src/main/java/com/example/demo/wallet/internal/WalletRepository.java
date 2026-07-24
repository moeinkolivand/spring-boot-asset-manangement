package com.example.demo.wallet.internal;

import com.example.demo.currency.Currency;
import com.example.demo.wallet.Wallet;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    boolean existsByUserIdAndCurrency(Long userId, Currency currency);
    boolean existsByUserAndCurrencyName(Long userId, String currencyName);
    List<Wallet> findByUserId(Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Wallet w WHERE w.id = :id")
    Optional<Wallet> findByIdForUpdate(@Param("id") Long id);
    Optional<Wallet> findByCurrencyNameAndUser(String currencyName, Long userId);
}
