package com.voip.calc.application.service;

import com.voip.calc.application.dto.CallContextRequest;
import com.voip.calc.application.dto.RateResponse;
import com.voip.calc.domain.model.CallContext;
import com.voip.calc.domain.service.RateCalculationEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Application-layer service — orchestrates rate calculation workflow.
 * Handles input transformation and delegates computation to domain layer.
 */
@Service
public class RateCalculationAppService {

    private final RateCalculationEngine engine;

    @Autowired
    public RateCalculationAppService(RateCalculationEngine engine) {
        this.engine = engine;
    }

    public RateResponse calculateRate(CallContextRequest request) {
        CallContext context = CallContext.of(
            request.callerNumber(),
            request.calleeNumber(),
            request.customerType(),
            request.callStartTime()
        );
        return RateResponse.from(engine.calculateRate(context));
    }
}
