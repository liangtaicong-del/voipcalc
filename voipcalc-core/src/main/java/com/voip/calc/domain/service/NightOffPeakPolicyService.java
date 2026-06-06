package com.voip.calc.domain.service;

import com.voip.calc.domain.model.RateZone;
import com.voip.calc.domain.model.RateZoneResolver;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Domain service — resolves off-peak rate reduction from call start time.
 * Night off-peak window: 23:00–05:00 next day.
 * During this window, ¥0.02 is subtracted from the per-minute rate.
 * Stateless and side-effect free.
 */
@Service
public class NightOffPeakPolicyService {

    public static final BigDecimal OFF_PEAK_REDUCTION = new BigDecimal("0.02");
    public static final BigDecimal ZERO_REDUCTION = BigDecimal.ZERO;

    public BigDecimal resolveOffPeakReduction(LocalDateTime callStartTime) {
        if (callStartTime == null) {
            return ZERO_REDUCTION;
        }
        RateZone zone = RateZoneResolver.resolve(callStartTime.toLocalTime());
        return zone.isNightOffPeak() ? OFF_PEAK_REDUCTION : ZERO_REDUCTION;
    }

    public boolean isNightOffPeak(LocalDateTime callStartTime) {
        if (callStartTime == null) {
            return false;
        }
        return RateZoneResolver.resolve(callStartTime.toLocalTime()).isNightOffPeak();
    }
}
