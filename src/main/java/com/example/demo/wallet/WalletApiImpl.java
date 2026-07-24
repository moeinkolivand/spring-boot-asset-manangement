package com.example.demo.wallet;

import com.example.demo.currency.Currency;
import com.example.demo.wallet.internal.WalletRepository;
import com.example.demo.wallet.internal.WalletStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class WalletApiImpl implements WalletApi {

    private final WalletRepository walletRepository;

    @Autowired
    public WalletApiImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public Optional<Wallet> findByIdForUpdate(Long id) {
        return walletRepository.findByIdForUpdate(id);
    }

    @Override
    public Optional<Wallet> findByCurrencyNameAndUser(String currencyName, Long userId) {
        return walletRepository.findByCurrencyNameAndUser(currencyName, userId);
    }

    @Override
    public void createWallet(Wallet wallet) {
        walletRepository.save(wallet);
    }

    @Override
    public boolean isWalletFrozen(Wallet wallet) {
        return wallet.getWalletStatus() == WalletStatus.FROZEN;
    }

    @Override
    public void createWallet(String name, BigDecimal balance, Long userId, Currency currency) {
        walletRepository.save(new Wallet(name, balance, userId, currency));
    }

}
