package com.example.demo.wallet;

public class ForbiddenActionOnFreezeWalletException extends RuntimeException {
    public ForbiddenActionOnFreezeWalletException(String message) {
        super(message);
    }
}
