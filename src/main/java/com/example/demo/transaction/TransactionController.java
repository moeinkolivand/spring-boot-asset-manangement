package com.example.demo.transaction;

import com.example.demo.user.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transaction/")
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("withdraw")
    public ResponseEntity<String> withdraw(@AuthenticationPrincipal User user, @Valid @RequestBody WithdrawDto withdrawDto) {
        transactionService.withdraw(withdrawDto, user);
        return ResponseEntity.ok("");
    }


    @PostMapping("deposit")
    public ResponseEntity<String> deposit(@AuthenticationPrincipal User user, @Valid @RequestBody DepositDto depositDto) {
        transactionService.deposit(depositDto, user);
        return ResponseEntity.ok("");
    }

}
