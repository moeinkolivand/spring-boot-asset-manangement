package com.example.demo.wallet;

import com.example.demo.currency.Currency;
import com.example.demo.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    boolean existsByUserAndCurrency(User user, Currency currency);

    List<Wallet> findByUser(User user);

}
