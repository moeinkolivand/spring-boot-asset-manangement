package com.example.demo.transaction.internal;

import com.example.demo.transaction.LedgerApi;
import com.example.demo.transaction.Transaction;
import com.example.demo.wallet.ForbiddenActionOnFreezeWalletException;
import com.example.demo.wallet.InsufficientWalletBalanceException;
import com.example.demo.wallet.Wallet;
import com.example.demo.wallet.WalletApi;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletApi walletApi;
    private final LedgerApi ledgerApi;
    private final TransactionTemplate transactionTemplate;

    @Autowired
    public TransactionService(
            TransactionRepository transactionRepository,
            WalletApi walletApi,
            LedgerApi ledgerApi,
            TransactionTemplate transactionTemplate
    ) {
        this.transactionRepository = transactionRepository;
        this.walletApi = walletApi;
        this.ledgerApi = ledgerApi;
        this.transactionTemplate = transactionTemplate;
    }

    private record WithdrawalWallets(Wallet userWallet, Wallet systemWallet) { }

    private WithdrawalWallets resolveAndValidate(String currencyName, Long user) {
        Wallet userWallet = walletApi.findByCurrencyNameAndUser(currencyName, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet not found"));
        Wallet systemWallet = walletApi.findByCurrencyNameAndUser(currencyName, user).orElseThrow(
                () -> new EntityNotFoundException("System Wallet With This Currency Not Found")
        );
        if (walletApi.isWalletFrozen(userWallet)) {
            throw new ForbiddenActionOnFreezeWalletException("Wallet Is Freeze And You Cant Do Any Action WithIt");
        }
        if (walletApi.isWalletFrozen(systemWallet)) {
            throw new ForbiddenActionOnFreezeWalletException("The Withdraw On This Currency " + currencyName + "Is Freeze For Now");
        }
        return new WithdrawalWallets(userWallet, systemWallet);
    }

    public void withdraw(WithdrawDto withdrawDto, Long user) {
        transactionRepository.findByIdempotencyKey(withdrawDto.idempotencyKey())
                .ifPresent(existing -> {
                    throw new TransactionAlreadyProccesedException("Already processed");
                });
        WithdrawalWallets withdrawalWallets = resolveAndValidate(withdrawDto.currencyName(), user);
        transactionTemplate.execute(status -> {
            Wallet userWallet = walletApi.findByIdForUpdate(withdrawalWallets.userWallet.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet not found"));
            if (isWalletBalanceGoesToNegative(userWallet, withdrawDto.amount())) {
                throw new InsufficientWalletBalanceException("Insufficient Wallet Balance");
            }
            Wallet systemWallet = walletApi.findByIdForUpdate(withdrawalWallets.systemWallet.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet not found"));
            Transaction transaction = transactionRepository.save(new Transaction(user, TransactionType.WITHDRAW, TransactionStatus.COMPLETED, withdrawDto.idempotencyKey()));
            userWallet.setBalance(userWallet.getBalance().subtract(withdrawDto.amount()));
            systemWallet.setBalance(systemWallet.getBalance().add(withdrawDto.amount()));
            walletApi.createWallet(userWallet);
            walletApi.createWallet(systemWallet);
            ledgerApi.createDebit(transaction, userWallet, withdrawDto.amount());
            ledgerApi.createCredit(transaction, systemWallet, withdrawDto.amount());
            return null;
        });
    }

    private boolean isWalletBalanceGoesToNegative(Wallet wallet, BigDecimal amount) {
        return wallet.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 0;
    }

    public void deposit(DepositDto depositDto, Long user) {
        transactionRepository.findByIdempotencyKey(depositDto.idempotencyKey())
                .ifPresent(existing -> {
                    throw new TransactionAlreadyProccesedException("Already processed");
                });
        WithdrawalWallets withdrawalWallets = resolveAndValidate(depositDto.currencyName(), user);
        transactionTemplate.execute(status -> {
            Wallet userWallet = walletApi.findByIdForUpdate(withdrawalWallets.userWallet.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet not found"));
            Wallet systemWallet = walletApi.findByIdForUpdate(withdrawalWallets.systemWallet.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet not found"));
            if (isWalletBalanceGoesToNegative(systemWallet, depositDto.amount())) {
                throw new InsufficientWalletBalanceException("Insufficient Balance");
            }
            Transaction transaction = transactionRepository.save(new Transaction(
                    user, TransactionType.DEPOSIT, TransactionStatus.COMPLETED, depositDto.idempotencyKey()
            ));
            ledgerApi.createDebit(transaction, systemWallet, depositDto.amount());
            ledgerApi.createCredit(transaction, userWallet,  depositDto.amount());
            return null;
        });

    }

    public Page<Transaction> getUserTransactions(Long user, Pageable pageable) {
        Pageable newPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().descending());
        return transactionRepository.findAllByUser(user, newPageable);
    }

}
