package com.example.demo.ledger;

import com.example.demo.ledger.internal.LedgerEntryDirectionEnum;
import com.example.demo.ledger.internal.LedgerRepository;
import com.example.demo.transaction.Transaction;
import com.example.demo.wallet.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class LedgerApiImpl implements LedgerApi {

    private final LedgerRepository ledgerRepository;

    @Autowired
    public LedgerApiImpl(LedgerRepository ledgerRepository) {
        this.ledgerRepository = ledgerRepository;
    }

    @Override
    public void createDebit(Transaction transaction, Wallet wallet, BigDecimal amount) {
        ledgerRepository.save(new LedgerEntry(transaction, wallet, LedgerEntryDirectionEnum.CREDIT, amount));
    }

    @Override
    public void createCredit(Transaction transaction, Wallet wallet, BigDecimal amount) {
        ledgerRepository.save(new LedgerEntry(transaction, wallet, LedgerEntryDirectionEnum.DEBIT, amount));

    }
}
