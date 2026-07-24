package com.example.demo.currency.internal;


import com.example.demo.currency.Currency;

import java.util.Optional;

public interface CurrencyApi {
    Optional<Currency> getCurrencyByName(String currencyName);

    Currency getReferenceById(Long id);

    Optional<Currency> findById(Long id);
}
