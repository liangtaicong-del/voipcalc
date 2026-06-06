package com.voip.calc.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CountryCode")
class CountryCodeTest {

    @ParameterizedTest
    @CsvSource({
        "+8613812345678, CN",
        "8613812345678, CN",
        "+14155551234, US",
        "14155551234, US",
        "+441234567890, OTHER",
        "+33612345678, OTHER",
        "+811234567, OTHER"
    })
    @DisplayName("Correctly resolves country code from phone number")
    void resolve_country_code(String phoneNumber, CountryCode expected) {
        assertEquals(expected, CountryCode.fromPhoneNumber(phoneNumber));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t"})
    @DisplayName("Returns OTHER for null or blank input")
    void null_or_blank(String input) {
        assertEquals(CountryCode.OTHER, CountryCode.fromPhoneNumber(input));
    }

    @Nested
    @DisplayName("Base rates")
    class BaseRates {
        @Test
        @DisplayName("CN base rate is 0.10")
        void cn_rate() { assertEquals(new BigDecimal("0.10"), CountryCode.CN.getBaseRate()); }
        @Test
        @DisplayName("US base rate is 0.05")
        void us_rate() { assertEquals(new BigDecimal("0.05"), CountryCode.US.getBaseRate()); }
        @Test
        @DisplayName("OTHER base rate is 0.50")
        void other_rate() { assertEquals(new BigDecimal("0.50"), CountryCode.OTHER.getBaseRate()); }
    }
}
