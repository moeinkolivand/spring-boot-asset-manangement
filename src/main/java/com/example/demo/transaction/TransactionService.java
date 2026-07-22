package com.example.demo.transaction;

import com.example.demo.currency.CurrencyRepository;
import com.example.demo.ledger.LedgerEntry;
import com.example.demo.ledger.LedgerEntryDirectionEnum;
import com.example.demo.ledger.LedgerRepository;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
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
    private final UserRepository userRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, WalletRepository walletRepository, LedgerRepository ledgerRepository, CurrencyRepository currencyRepository, TransactionTemplate transactionTemplate, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.ledgerRepository = ledgerRepository;
        this.currencyRepository = currencyRepository;
        this.transactionTemplate = transactionTemplate;
        this.userRepository = userRepository;
    }

    private record WithdrawalWallets(Wallet userWallet, Wallet systemWallet) {
    }

    private WithdrawalWallets resolveAndValidate(WithdrawDto dto, User user) {
        Wallet userWallet = walletRepository.findByCurrencyNameAndUser(dto.currencyName(), user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet not found"));
        User adminUser = userRepository.findByUsername("09123456789").orElseThrow(() -> new RuntimeException("User Admin Does Not Exists"));
        Wallet systemWallet = walletRepository.findByCurrencyNameAndUser(dto.currencyName(), adminUser).orElseThrow(
                () -> new EntityNotFoundException("System Wallet With This Currency Not Found")
        );
        if (isWalletFrozen(userWallet)) {
            throw new ForbiddenActionOnFreezeWalletException("Wallet Is Freeze And You Cant Do Any Action WithIt");
        }
        if (isWalletFrozen(systemWallet)) {
            throw new ForbiddenActionOnFreezeWalletException("The Withdraw On This Currency " + dto.currencyName() + "Is Freeze For Now");
        }
        return new WithdrawalWallets(userWallet, systemWallet);
    }

    public void withdraw(WithdrawDto withdrawDto, User user) {
        transactionRepository.findByIdempotencyKey(withdrawDto.idempotencyKey())
                .ifPresent(existing -> {
                    throw new TransactionAlreadyProccesedException("Already processed");
                });
        WithdrawalWallets withdrawalWallets = resolveAndValidate(withdrawDto, user);
        transactionTemplate.execute(status -> {
            Wallet userWallet = walletRepository.findByIdForUpdate(withdrawalWallets.userWallet.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet not found"));
            if (isWalletBalanceGoesToNegative(userWallet, withdrawDto.amount())) {
                throw new InsufficientWalletBalanceException("Insufficient Wallet Balance");
            }
            Wallet systemWallet = walletRepository.findByIdForUpdate(withdrawalWallets.systemWallet.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet not found"));
            Transaction transaction = transactionRepository.save(new Transaction(user, TransactionType.WITHDRAW, TransactionStatus.COMPLETED, withdrawDto.idempotencyKey()));
            userWallet.setBalance(userWallet.getBalance().subtract(withdrawDto.amount()));
            systemWallet.setBalance(systemWallet.getBalance().add(withdrawDto.amount()));
            walletRepository.save(userWallet);
            walletRepository.save(systemWallet);
            ledgerRepository.save(new LedgerEntry(transaction, userWallet, LedgerEntryDirectionEnum.DEBIT, withdrawDto.amount()));
            ledgerRepository.save(new LedgerEntry(transaction, systemWallet, LedgerEntryDirectionEnum.CREDIT, withdrawDto.amount()));
            return null;
        });
    }

    private boolean isWalletFrozen(Wallet wallet) {
        return wallet.getWalletStatus() == WalletStatus.FROZEN;
    }

    private boolean isWalletBalanceGoesToNegative(Wallet wallet, BigDecimal amount) {
        return wallet.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 0;
    }

    public void deposit() {

    }

}
