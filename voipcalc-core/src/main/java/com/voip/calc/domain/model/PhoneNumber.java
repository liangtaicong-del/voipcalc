package com.voip.calc.domain.model;

import java.util.Objects;

/**
 * Value object encapsulating a phone number and its derived country code.
 * The phone number itself is stored in normalized form (strips leading zeros).
 */
public final class PhoneNumber {
    private final String rawNumber;
    private final CountryCode countryCode;

    public PhoneNumber(String rawNumber, CountryCode countryCode) {
        this.rawNumber = normalize(Objects.requireNonNull(rawNumber, "rawNumber must not be null"));
        this.countryCode = Objects.requireNonNull(countryCode, "countryCode must not be null");
    }

    public static PhoneNumber of(String number) {
        if (number == null || number.isBlank()) {
            return new PhoneNumber("+0", CountryCode.OTHER);
        }
        return new PhoneNumber(number, CountryCode.fromPhoneNumber(number));
    }

    private static String normalize(String number) {
        String n = number.trim();
        if (!n.startsWith("+")) {
            n = "+" + n;
        }
        return n;
    }

    public String getRawNumber() {
        return rawNumber;
    }

    public CountryCode getCountryCode() {
        return countryCode;
    }

    public String getCountryPrefix() {
        return countryCode.getPrefix();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneNumber that = (PhoneNumber) o;
        return rawNumber.equals(that.rawNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rawNumber);
    }

    @Override
    public String toString() {
        return rawNumber;
    }
}
