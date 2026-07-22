package com.example.demo.wallet;

import com.example.demo.currency.Currency;
import com.example.demo.user.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    boolean existsByUserAndCurrency(User user, Currency currency);
    boolean existsByUserAndCurrencyName(User user, String currencyName);
    List<Wallet> findByUser(User user);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Wallet w WHERE w.id = :id")
    Optional<Wallet> findByIdForUpdate(@Param("id") Long id);
    Optional<Wallet> findByCurrencyNameAndUser(String currencyName, User user);
}
