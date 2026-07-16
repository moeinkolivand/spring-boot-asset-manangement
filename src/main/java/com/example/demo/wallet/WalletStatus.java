package com.example.demo.wallet;

public enum WalletStatus {

    ACTIVE(1, "Active"),
    INACTIVE(2, "Inactive");

    private final int value;
    private final String label;

    WalletStatus(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public static WalletStatus fromValue(int value) {
        for (WalletStatus status : WalletStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status value: " + value);
    }
}