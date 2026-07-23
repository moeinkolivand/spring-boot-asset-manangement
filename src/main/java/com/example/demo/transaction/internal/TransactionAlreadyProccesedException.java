package com.example.demo.transaction.internal;

public class TransactionAlreadyProccesedException extends RuntimeException {
    public TransactionAlreadyProccesedException(String message) {
        super(message);
    }
}
