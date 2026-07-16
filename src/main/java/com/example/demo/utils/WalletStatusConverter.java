package com.example.demo.utils;


import com.example.demo.wallet.WalletStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class WalletStatusConverter implements AttributeConverter<WalletStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(WalletStatus attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public WalletStatus convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return WalletStatus.fromValue(dbData);
    }
}