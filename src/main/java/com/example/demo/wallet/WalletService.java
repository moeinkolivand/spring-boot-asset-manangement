package com.example.demo.wallet;

import com.example.demo.wallet.dto.WalletRequestDto;
import com.example.demo.wallet.dto.WalletResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class WalletService {

    private final WalletRepository walletRepository;


    @Autowired
    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public WalletResponseDto createWallet(WalletRequestDto walletRequestDto) {
        Wallet wallet = new Wallet(walletRequestDto.name(), walletRequestDto.balance());
        walletRepository.save(wallet);
        return returnWalletResponse(wallet);
    }

    public WalletResponseDto getWalletWithId(Long id) {
        Wallet wallet = getWalletById(id);
        return returnWalletResponse(wallet);
    }


    public WalletResponseDto updateWallet(Long id, WalletRequestDto walletRequestDto) {
        Wallet wallet = getWalletById(id);
        wallet.setBalance(walletRequestDto.balance());
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
                wallet.getCreatedAt()
        );
    }

}
