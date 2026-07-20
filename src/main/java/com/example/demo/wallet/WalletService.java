package com.example.demo.wallet;

import com.example.demo.currency.Currency;
import com.example.demo.currency.CurrencyRepository;
import com.example.demo.user.User;
import com.example.demo.wallet.dto.WalletRequestDto;
import com.example.demo.wallet.dto.WalletResponseDto;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final CurrencyRepository currencyRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository, CurrencyRepository currencyRepository) {
        this.walletRepository = walletRepository;
        this.currencyRepository = currencyRepository;
    }

    public WalletResponseDto createWallet(WalletRequestDto walletRequestDto, User currentUser) {
        Currency currency = currencyRepository.findById(walletRequestDto.currencyId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Currency Not Found")
        );
        if (walletRepository.existsByUserAndCurrency(currentUser, currency)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Wallet With Currency " + currency.getName() + " exists");
        }
        Wallet wallet = new Wallet(walletRequestDto.name(), BigDecimal.ZERO, currentUser, currency);
        walletRepository.save(wallet);
        return returnWalletResponse(wallet);
    }

    public WalletResponseDto getWalletWithId(Long id) {
        Wallet wallet = getWalletById(id);
        return returnWalletResponse(wallet);
    }


    public WalletResponseDto updateWallet(Long id, WalletRequestDto walletRequestDto) {
        Wallet wallet = getWalletById(id);
        wallet.setName(walletRequestDto.name());
        Wallet updatedWallet = walletRepository.save(wallet);
        return returnWalletResponse(updatedWallet);
    }

    private Wallet getWalletById(Long id) {
        return walletRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Wallet Not Found"));
    }

    private WalletResponseDto returnWalletResponse(Wallet wallet) {
        return new WalletResponseDto(
                wallet.getName(),
                wallet.getId(),
                wallet.getBalance(),
                wallet.getCurrency(),
                wallet.getCreatedAt()
        );
    }

    public @Nullable List<WalletResponseDto> getUserWallets(User user) {
        return walletRepository.findByUser(user).stream().map(this::returnWalletResponse).toList();
    }

}
