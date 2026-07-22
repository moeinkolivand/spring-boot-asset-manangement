package com.example.demo.transaction;

public class TransactionAlreadyProccesedException extends RuntimeException {
    public TransactionAlreadyProccesedException(String message) {
        super(message);
    }
}
