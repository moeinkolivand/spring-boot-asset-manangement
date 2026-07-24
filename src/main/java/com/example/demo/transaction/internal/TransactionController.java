package com.example.demo.transaction.internal;

import com.example.demo.transaction.Transaction;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transaction/")
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("withdraw")
    public ResponseEntity<String> withdraw(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @Valid @RequestBody WithdrawDto withdrawDto
    ) {
        transactionService.withdraw(withdrawDto, userId);
        return ResponseEntity.ok("");
    }


    @PostMapping("deposit")
    public ResponseEntity<String> deposit(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @Valid @RequestBody DepositDto depositDto
    ) {
        transactionService.deposit(depositDto, userId);
        return ResponseEntity.ok("");
    }

    @GetMapping
    public ResponseEntity<Page<Transaction>> getUserTransactions(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @PageableDefault(
            size = 10, sort = "created_at", direction = Sort.Direction.DESC
    )Pageable pageable){
        return ResponseEntity.ok(transactionService.getUserTransactions(userId, pageable));
    }

}
