package com.monarkmarkets.dtos.preipocompany;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum representing the entity type of a PreIPOCompany.
 */
public enum CompanyType {
    SOLE_PROPRIETOR("SOLE_PROPRIETOR"),
    SINGLE_MEMBER_LLC("SINGLE_MEMBER_LLC"),
    LLC("LLC"),
    PARTNERSHIP("PARTNERSHIP"),
    C_CORPORATION("C_CORPORATION"),
    S_CORPORATION("S_CORPORATION"),
    TRUST("TRUST"),
    IRA_OR_TAX_EXEMPT("IRA_OR_TAX_EXEMPT"),
    NON_US_CORPORATION("NON_US_CORPORATION"),
    PRIVATE_LIMITED_COMPANY("PRIVATE_LIMITED_COMPANY"),
    PUBLIC_LIMITED_COMPANY("PUBLIC_LIMITED_COMPANY"),
    TAX_EXEMPT_ENTITY("TAX_EXEMPT_ENTITY"),
    FOREIGN_TRUST("FOREIGN_TRUST"),
    OTHER("OTHER");

    private final String value;

    CompanyType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static CompanyType fromString(String key) {
        for (CompanyType type : values()) {
            if (type.value.equalsIgnoreCase(key)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid CompanyType: " + key);
    }
}
