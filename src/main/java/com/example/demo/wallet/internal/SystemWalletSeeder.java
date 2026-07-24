package com.example.demo.wallet.internal;

import com.example.demo.currency.Currency;
import com.example.demo.currency.CurrencyApiImpl;
import com.example.demo.user.internal.UserRepository;
import com.example.demo.wallet.Wallet;
import com.example.demo.wallet.WalletApiImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Order(3)
public class SystemWalletSeeder implements CommandLineRunner {

    private final WalletApiImpl walletRepository;
    private final UserRepository userRepository;
    private final CurrencyApiImpl currencyApi;

    @Autowired
    public SystemWalletSeeder(WalletApiImpl walletRepository,
                              UserRepository userRepository,
                              CurrencyApiImpl currencyApi) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.currencyApi = currencyApi;
    }

    @Override
    @Transactional
    public void run(String... args) {

        String adminPhone = "09123456789";
        Long adminUser = userRepository.findIdByPhoneNumber(adminPhone)
                .orElseThrow(() -> new IllegalStateException(
                        "Admin user not found! Please ensure AdminUserSeeder (@Order(1)) ran first."
                ));

        String currencyName = "USDT";
        Currency usdtCurrency = currencyApi.getCurrencyByName(currencyName)
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

            walletRepository.createWallet(systemWallet);

            System.out.println("System wallet created successfully for Admin with 1,000,000 USDT!");
        } else {
            System.out.println("System wallet already exists for Admin + USDT. Skipping.");
        }
    }
}