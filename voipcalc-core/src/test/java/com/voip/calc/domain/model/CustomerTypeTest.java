package com.voip.calc.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CustomerType")
class CustomerTypeTest {

    @ParameterizedTest
    @CsvSource({
        "VIP, VIP",
        "vip, VIP",
        "Vip, VIP",
        "NORMAL, NORMAL",
        "normal, NORMAL",
        "anything, NORMAL"
    })
    @DisplayName("Correctly resolves customer type from string")
    void resolve_customer_type(String input, CustomerType expected) {
        assertEquals(expected, CustomerType.fromString(input));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t"})
    @DisplayName("Defaults to NORMAL for null or blank input")
    void null_or_blank(String input) {
        assertEquals(CustomerType.NORMAL, CustomerType.fromString(input));
    }

    @Test
    @DisplayName("VIP discount factor is 0.9")
    void vip_discount() {
        assertEquals(new BigDecimal("0.9"), CustomerType.VIP.getDiscountFactor());
    }

    @Test
    @DisplayName("NORMAL discount factor is 1.0")
    void normal_discount() {
        assertEquals(BigDecimal.ONE, CustomerType.NORMAL.getDiscountFactor());
    }
}
