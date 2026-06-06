package com.voip.calc.domain.service;

import com.voip.calc.domain.model.CallContext;
import com.voip.calc.domain.model.RateResult;
import com.voip.calc.domain.model.RateZone;
import com.voip.calc.domain.model.RateZoneResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Core domain service — orchestrates all three pricing policies
 * to compute the final per-minute rate for a call.
 *
 * Formula:
 *   discountedRate = baseRate * discountFactor
 *   finalRate = max(0, discountedRate - offPeakReduction)
 *
 * This service is stateless and has no side effects.
 */
@Service
public class RateCalculationEngine {

    private final BaseRatePolicyService baseRatePolicy;
    private final CustomerDiscountPolicyService customerDiscountPolicy;
    private final NightOffPeakPolicyService nightOffPeakPolicy;

    @Autowired
    public RateCalculationEngine(BaseRatePolicyService baseRatePolicy,
                                  CustomerDiscountPolicyService customerDiscountPolicy,
                                  NightOffPeakPolicyService nightOffPeakPolicy) {
        this.baseRatePolicy = baseRatePolicy;
        this.customerDiscountPolicy = customerDiscountPolicy;
        this.nightOffPeakPolicy = nightOffPeakPolicy;
    }

    /**
     * Calculate the final per-minute rate for the given call context.
     *
     * @param context the call context containing caller, callee, customer type, and call time
     * @return the computed RateResult with full audit trail
     */
    public RateResult calculateRate(CallContext context) {
        BigDecimal baseRate = baseRatePolicy.resolveRate(context.getDestinationCountry());

        BigDecimal discountFactor = customerDiscountPolicy.resolveDiscountFactor(context.getCustomerType());

        BigDecimal offPeakReduction = nightOffPeakPolicy.resolveOffPeakReduction(context.getCallStartTime());

        BigDecimal discountedRate = baseRate.multiply(discountFactor).setScale(4, RoundingMode.HALF_UP);

        BigDecimal rawFinalRate = discountedRate.subtract(offPeakReduction);
        BigDecimal finalRate = rawFinalRate.compareTo(BigDecimal.ZERO) < 0
            ? BigDecimal.ZERO : rawFinalRate;

        RateZone rateZone = RateZoneResolver.resolve(context.getCallStartTime().toLocalTime());

        RateResult.Breakdown breakdown = new RateResult.Breakdown(
            context.getDestinationCountry().getPrefix(),
            context.getDestinationCountry().getDisplayName(),
            context.getCustomerType().name(),
            nightOffPeakPolicy.isNightOffPeak(context.getCallStartTime()),
            rateZone.name()
        );

        return RateResult.builder()
            .baseRate(baseRate)
            .discountFactor(discountFactor)
            .offPeakReduction(offPeakReduction)
            .finalRate(finalRate)
            .currency("CNY")
            .breakdown(breakdown)
            .build();
    }
}
