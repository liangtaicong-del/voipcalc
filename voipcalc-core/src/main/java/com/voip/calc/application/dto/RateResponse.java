package com.voip.calc.application.dto;

import com.voip.calc.domain.model.RateResult;

import java.math.BigDecimal;

/**
 * Application-layer DTO for rate calculation response.
 * Converts from domain RateResult to a clean API response.
 */
public record RateResponse(
    BigDecimal baseRate,
    BigDecimal discountFactor,
    BigDecimal offPeakReduction,
    BigDecimal finalRate,
    String currency,
    Breakdown breakdown
) {

    public record Breakdown(
        String countryCode,
        String countryName,
        String customerType,
        boolean isNightOffPeak,
        String rateZone
    ) {}

    public static RateResponse from(RateResult result) {
        RateResult.Breakdown bd = result.getBreakdown();
        return new RateResponse(
            result.getBaseRate(),
            result.getDiscountFactor(),
            result.getOffPeakReduction(),
            result.getFinalRate(),
            result.getCurrency(),
            new Breakdown(
                bd.getCountryCode(),
                bd.getCountryName(),
                bd.getCustomerType(),
                bd.isNightOffPeak(),
                bd.getRateZone()
            )
        );
    }
}
