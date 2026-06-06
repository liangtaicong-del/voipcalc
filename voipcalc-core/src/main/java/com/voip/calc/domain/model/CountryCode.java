package com.voip.calc.domain.model;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Country code enum for call destination.
 * Only +86 (CN) and +1 (US) have special rates;
 * all other country codes fall back to the default OTHER rate.
 */
public enum CountryCode {
    CN("+86", "China", new BigDecimal("0.10")),
    US("+1", "United States", new BigDecimal("0.05")),
    OTHER("+0", "Other Countries", new BigDecimal("0.50"));

    private final String prefix;
    private final String displayName;
    private final BigDecimal baseRate;

    CountryCode(String prefix, String displayName, BigDecimal baseRate) {
        this.prefix = prefix;
        this.displayName = displayName;
        this.baseRate = baseRate;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getDisplayName() {
        return displayName;
    }

    public BigDecimal getBaseRate() {
        return baseRate;
    }

    /**
     * Resolve the country code from a phone number string.
     * Matches the longest prefix that the number starts with.
     */
    public static CountryCode fromPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return OTHER;
        }
        if (phoneNumber.startsWith("+86") || phoneNumber.startsWith("86")) {
            return CN;
        }
        if (phoneNumber.startsWith("+1") || phoneNumber.startsWith("1")) {
            return US;
        }
        return OTHER;
    }

    public Optional<String> getCountryName() {
        return Optional.of(displayName);
    }
}
