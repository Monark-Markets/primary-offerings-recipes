package com.monarkmarkets.dtos.preipocompanyinvestment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum representing the transaction type of a Pre-IPO company investment.
 */
public enum TransactionType {
    PRIMARY("Primary"),
    SECONDARY("Secondary"),
    TENDER_OFFER("TenderOffer");

    private final String value;

    TransactionType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static TransactionType fromString(String key) {
        for (TransactionType type : values()) {
            if (type.value.equalsIgnoreCase(key)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid TransactionType: " + key);
    }
}
