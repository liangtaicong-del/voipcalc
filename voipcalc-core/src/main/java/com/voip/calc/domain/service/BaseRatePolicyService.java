package com.voip.calc.domain.service;

import com.voip.calc.domain.model.CountryCode;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Domain service — resolves the base rate from the destination country code.
 * Stateless and side-effect free.
 */
@Service
public class BaseRatePolicyService {

    public BigDecimal resolveRate(CountryCode countryCode) {
        if (countryCode == null) {
            return CountryCode.OTHER.getBaseRate();
        }
        return countryCode.getBaseRate();
    }
}
