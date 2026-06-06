package com.voip.calc.application.service;

import com.voip.calc.application.dto.CallContextRequest;
import com.voip.calc.application.dto.RateResponse;
import com.voip.calc.domain.model.CallContext;
import com.voip.calc.domain.model.RateResult;
import com.voip.calc.domain.service.RateCalculationEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RateCalculationAppService")
class RateCalculationAppServiceTest {

    @Mock
    private RateCalculationEngine engine;

    private RateCalculationAppService appService;

    @BeforeEach
    void setUp() {
        appService = new RateCalculationAppService(engine);
    }

    @Test
    @DisplayName("Delegates to engine and transforms result to response DTO")
    void delegates_to_engine() {
        RateResult mockResult = RateResult.builder()
            .baseRate(new BigDecimal("0.05"))
            .discountFactor(new BigDecimal("0.9"))
            .offPeakReduction(new BigDecimal("0.02"))
            .finalRate(new BigDecimal("0.025"))
            .currency("CNY")
            .breakdown(new RateResult.Breakdown("+1", "US", "VIP", true, "NIGHT_OFF_PEAK"))
            .build();

        when(engine.calculateRate(any(CallContext.class))).thenReturn(mockResult);

        CallContextRequest request = new CallContextRequest(
            "+8613812345678", "+14155551234", "VIP", LocalDateTime.of(2024, 6, 6, 2, 30)
        );

        RateResponse response = appService.calculateRate(request);

        assertEquals(new BigDecimal("0.05"), response.baseRate());
        assertEquals(new BigDecimal("0.9"), response.discountFactor());
        assertEquals(new BigDecimal("0.02"), response.offPeakReduction());
        assertEquals(new BigDecimal("0.025"), response.finalRate());
        assertEquals("CNY", response.currency());
        assertEquals("+1", response.breakdown().countryCode());
        assertTrue(response.breakdown().isNightOffPeak());

        verify(engine, times(1)).calculateRate(any(CallContext.class));
    }
}
