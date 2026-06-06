package com.voip.calc.interface.controller;

import com.voip.calc.application.service.RateCalculationAppService;
import com.voip.calc.domain.model.RateResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RateController.class)
@DisplayName("RateController REST API")
class RateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RateCalculationAppService appService;

    @Test
    @DisplayName("GET /api/rate returns 200 with computed rate")
    void calculate_rate_returns_200() throws Exception {
        RateResult mockResult = RateResult.builder()
            .baseRate(new BigDecimal("0.05"))
            .discountFactor(new BigDecimal("0.9"))
            .offPeakReduction(new BigDecimal("0.02"))
            .finalRate(new BigDecimal("0.025"))
            .currency("CNY")
            .breakdown(new RateResult.Breakdown("+1", "US", "VIP", true, "NIGHT_OFF_PEAK"))
            .build();

        when(appService.calculateRate(any())).thenReturn(
            com.voip.calc.application.dto.RateResponse.from(mockResult)
        );

        mockMvc.perform(get("/api/rate")
                .param("callerNumber", "+8613812345678")
                .param("calleeNumber", "+14155551234")
                .param("customerType", "VIP")
                .param("callStartTime", "2024-06-06T02:30:00")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.finalRate").value(0.025))
            .andExpect(jsonPath("$.breakdown.countryCode").value("+1"))
            .andExpect(jsonPath("$.breakdown.isNightOffPeak").value(true));
    }

    @Test
    @DisplayName("GET /api/rate with missing params returns 400")
    void calculate_rate_missing_params_returns_400() throws Exception {
        mockMvc.perform(get("/api/rate")
                .param("callerNumber", "+8613812345678")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }
}
