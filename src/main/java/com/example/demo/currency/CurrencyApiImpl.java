package com.example.demo.currency;

import com.example.demo.currency.internal.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CurrencyApiImpl implements CurrencyApi {
    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyApiImpl(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }


    @Override
    public Optional<Currency> getCurrencyByName(String currencyName) {
        return currencyRepository.findByName(currencyName);
    }

    @Override
    public Currency getReferenceById(Long id) {
        return currencyRepository.getReferenceById(id);
    }

    @Override
    public Optional<Currency> findById(Long id) {
        return currencyRepository.findById(id);
    }


}
