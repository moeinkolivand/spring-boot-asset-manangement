package com.example.demo.currency;

import jakarta.persistence.*;

@Entity
@Table(name = "currencies")
public class Currecy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    public Currecy() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
