package com.voip.calc.domain.model;

import java.math.BigDecimal;

/**
 * Customer type enum determining discount policy.
 * VIP customers (overseas students / overseas Chinese card holders) receive a 10% discount.
 */
public enum CustomerType {
    VIP("overseas_student_chinese_card", new BigDecimal("0.9")),
    NORMAL("regular_user", BigDecimal.ONE);

    private final String description;
    private final BigDecimal discountFactor;

    CustomerType(String description, BigDecimal discountFactor) {
        this.description = description;
        this.discountFactor = discountFactor;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getDiscountFactor() {
        return discountFactor;
    }

    public static CustomerType fromString(String value) {
        if (value == null || value.isBlank()) {
            return NORMAL;
        }
        return switch (value.toUpperCase().trim()) {
            case "VIP", "Vip", "vip" -> VIP;
            default -> NORMAL;
        };
    }
}
