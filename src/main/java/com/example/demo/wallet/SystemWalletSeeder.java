package com.example.demo.wallet;

import com.example.demo.currency.Currency;
import com.example.demo.currency.CurrencyRepository;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Order(3) // Runs after UserSeeder (1) and CurrencySeeder (2)
public class SystemWalletSeeder implements CommandLineRunner {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;

    @Autowired
    public SystemWalletSeeder(WalletRepository walletRepository,
                              UserRepository userRepository,
                              CurrencyRepository currencyRepository) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.currencyRepository = currencyRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {

        String adminPhone = "09123456789";
        User adminUser = userRepository.findByPhoneNumber(adminPhone)
                .orElseThrow(() -> new IllegalStateException(
                        "Admin user not found! Please ensure AdminUserSeeder (@Order(1)) ran first."
                ));

        String currencyName = "USDT";
        Currency usdtCurrency = currencyRepository.findByName(currencyName)
                .orElseThrow(() -> new IllegalStateException(
                        "USDT currency not found! Please ensure CurrencySeeder (@Order(2)) ran first."
                ));

        if (walletRepository.findByCurrencyNameAndUser(currencyName, adminUser).isEmpty()) {
            Wallet systemWallet = new Wallet(
                    "SYSTEM_USDT_WALLET",
                    BigDecimal.valueOf(1_000_000.00),
                    adminUser,
                    usdtCurrency
            );
            systemWallet.setWalletStatus(WalletStatus.ACTIVE);

            walletRepository.save(systemWallet);

            System.out.println("System wallet created successfully for Admin with 1,000,000 USDT!");
        } else {
            System.out.println("System wallet already exists for Admin + USDT. Skipping.");
        }
    }
}