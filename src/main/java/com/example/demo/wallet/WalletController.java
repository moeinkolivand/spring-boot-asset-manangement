package com.example.demo.wallet;


import com.example.demo.user.User;
import com.example.demo.wallet.dto.WalletRequestDto;
import com.example.demo.wallet.dto.WalletResponseDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/wallets")
public class WalletController {
    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<WalletResponseDto> getWallet(@PathVariable Long id) {
        return ResponseEntity.ok(walletService.getWalletWithId(id));
    }

    @PostMapping
    public ResponseEntity<WalletResponseDto> createWallet(@Valid @RequestBody WalletRequestDto walletRequestDto, @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED).body(walletService.createWallet(walletRequestDto, currentUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WalletResponseDto> updateWallet(
            @PathVariable Long id,
            @Valid @RequestBody WalletRequestDto requestDto
    ) {
        return ResponseEntity.ok(walletService.updateWallet(id, requestDto));
    }

    @GetMapping
    public ResponseEntity<List<WalletResponseDto>> getUserWallets(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(walletService.getUserWallets(currentUser));
    }

}
