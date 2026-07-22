package com.example.demo.wallet;

import com.example.demo.currency.Currency;
import com.example.demo.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    boolean existsByUserAndCurrency(User user, Currency currency);
    List<Wallet> findByUser(User user);
    Optional<Wallet> findByCurrencyNameAndUser(String currencyName, User user);
}
