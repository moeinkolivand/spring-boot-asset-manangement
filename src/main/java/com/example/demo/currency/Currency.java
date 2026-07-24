package com.example.demo.currency;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "currencies")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(updatable = false, name = "created_at")
    private LocalDate createdAt;

    public Currency() {
    }

    public Currency(String name) {
        this.name = name;
        this.createdAt = LocalDate.now();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }
}
