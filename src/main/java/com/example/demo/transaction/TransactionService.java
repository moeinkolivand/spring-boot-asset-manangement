package com.example.demo.transaction;

import com.example.demo.currency.CurrencyRepository;
import com.example.demo.ledger.LedgerEntry;
import com.example.demo.ledger.LedgerEntryDirectionEnum;
import com.example.demo.ledger.LedgerRepository;
import com.example.demo.user.User;
import com.example.demo.wallet.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final LedgerRepository ledgerRepository;
    private final CurrencyRepository currencyRepository;
    private final TransactionTemplate transactionTemplate;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, WalletRepository walletRepository, LedgerRepository ledgerRepository, CurrencyRepository currencyRepository, TransactionTemplate transactionTemplate) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.ledgerRepository = ledgerRepository;
        this.currencyRepository = currencyRepository;
        this.transactionTemplate = transactionTemplate;
    }


    public void withdraw(WithdrawDto withdrawDto, User user) {
        transactionRepository.findByIdempotencyKey(withdrawDto.idempotencyKey())
                .ifPresent(existing -> {
                    throw new TransactionAlreadyProccesedException("Already processed");
                });

        Wallet userWallet = walletRepository.findByCurrencyNameAndUser(withdrawDto.currencyName(), user).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet Not Found!")
        );
        if (userWallet.getWalletStatus() == WalletStatus.FROZEN) {
            throw new ForbiddenActionOnFreezeWalletException("Wallet Is Freeze And You Cant Do Any Action WithIt");
        }
        if (userWallet.getBalance().subtract(withdrawDto.amount()).compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientWalletBalanceException("Insufficient Wallet Balance");
        }
        Wallet systemWallet = walletRepository.findByCurrencyNameAndUser(withdrawDto.currencyName(), user).orElseThrow(
                () -> new EntityNotFoundException("System Wallet With This Currency Not Found")
        );
        transactionTemplate.execute(status -> {
            Transaction transaction = transactionRepository.save(new Transaction(user, TransactionType.WITHDRAW, TransactionStatus.COMPLETED, withdrawDto.idempotencyKey()));
            userWallet.setBalance(userWallet.getBalance().subtract(withdrawDto.amount()));
            walletRepository.save(userWallet);
            ledgerRepository.save(new LedgerEntry(transaction, userWallet, LedgerEntryDirectionEnum.DEBIT, withdrawDto.amount()));
            ledgerRepository.save(new LedgerEntry(transaction, systemWallet, LedgerEntryDirectionEnum.CREDIT, withdrawDto.amount()));
            return null;
        });
    }


    public void deposit() {
    }


}
