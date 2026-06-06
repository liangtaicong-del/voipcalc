package com.voip.calc.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Immutable domain object representing the result of a rate calculation.
 * Contains both the final rate and a full audit trail of how it was computed.
 */
public final class RateResult {
    private final BigDecimal baseRate;
    private final BigDecimal discountFactor;
    private final BigDecimal offPeakReduction;
    private final BigDecimal finalRate;
    private final String currency;
    private final Breakdown breakdown;

    public RateResult(BigDecimal baseRate, BigDecimal discountFactor,
                      BigDecimal offPeakReduction, BigDecimal finalRate,
                      String currency, Breakdown breakdown) {
        this.baseRate = round(baseRate);
        this.discountFactor = round(discountFactor);
        this.offPeakReduction = round(offPeakReduction);
        this.finalRate = round(finalRate);
        this.currency = Objects.requireNonNull(currency);
        this.breakdown = Objects.requireNonNull(breakdown);
    }

    private static BigDecimal round(BigDecimal value) {
        if (value == null) return BigDecimal.ZERO;
        return value.setScale(4, RoundingMode.HALF_UP);
    }

    public BigDecimal getBaseRate() {
        return baseRate;
    }

    public BigDecimal getDiscountFactor() {
        return discountFactor;
    }

    public BigDecimal getOffPeakReduction() {
        return offPeakReduction;
    }

    public BigDecimal getFinalRate() {
        return finalRate;
    }

    public String getCurrency() {
        return currency;
    }

    public Breakdown getBreakdown() {
        return breakdown;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private BigDecimal baseRate;
        private BigDecimal discountFactor = BigDecimal.ONE;
        private BigDecimal offPeakReduction = BigDecimal.ZERO;
        private BigDecimal finalRate;
        private String currency = "CNY";
        private Breakdown breakdown;

        public Builder baseRate(BigDecimal baseRate) {
            this.baseRate = baseRate;
            return this;
        }

        public Builder discountFactor(BigDecimal discountFactor) {
            this.discountFactor = discountFactor;
            return this;
        }

        public Builder offPeakReduction(BigDecimal offPeakReduction) {
            this.offPeakReduction = offPeakReduction;
            return this;
        }

        public Builder finalRate(BigDecimal finalRate) {
            this.finalRate = finalRate;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder breakdown(Breakdown breakdown) {
            this.breakdown = breakdown;
            return this;
        }

        public RateResult build() {
            return new RateResult(baseRate, discountFactor, offPeakReduction, finalRate, currency, breakdown);
        }
    }

    /**
     * Immutable breakdown object providing full audit trail.
     */
    public static final class Breakdown {
        private final String countryCode;
        private final String countryName;
        private final String customerType;
        private final boolean isNightOffPeak;
        private final String rateZone;

        public Breakdown(String countryCode, String countryName, String customerType,
                         boolean isNightOffPeak, String rateZone) {
            this.countryCode = countryCode;
            this.countryName = countryName;
            this.customerType = customerType;
            this.isNightOffPeak = isNightOffPeak;
            this.rateZone = rateZone;
        }

        public String getCountryCode() { return countryCode; }
        public String getCountryName() { return countryName; }
        public String getCustomerType() { return customerType; }
        public boolean isNightOffPeak() { return isNightOffPeak; }
        public String getRateZone() { return rateZone; }
    }

    @Override
    public String toString() {
        return "RateResult{finalRate=" + finalRate + " " + currency + "/min}";
    }
}
