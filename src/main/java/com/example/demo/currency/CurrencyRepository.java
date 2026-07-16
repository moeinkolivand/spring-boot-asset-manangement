package com.example.demo.currency;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currecy, Long> {
    public Optional<Currecy> getByName(String name);

}
