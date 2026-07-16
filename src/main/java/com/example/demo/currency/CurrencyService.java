package com.example.demo.currency;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public void createCurrency(String name) {
        try {
            currencyRepository.save(new Currecy(name));
        } catch (DataIntegrityViolationException e) {
            throw new CurrencyAlreadyExistsException("Currency With Name: " + name + " Already Exists");
        }
    }

    public Currecy getCurrency(String name) {
        return currencyRepository.getByName(name).orElseThrow(() -> new EntityNotFoundException("Currency With Name: " + name + "Does Not Exists"));
    }
}
