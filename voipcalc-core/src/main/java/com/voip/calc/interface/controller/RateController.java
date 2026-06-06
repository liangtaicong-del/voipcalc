package com.voip.calc.interface.controller;

import com.voip.calc.application.dto.CallContextRequest;
import com.voip.calc.application.dto.RateResponse;
import com.voip.calc.application.service.RateCalculationAppService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * REST controller exposing the rate calculation API.
 * Transforms HTTP request parameters into application-layer DTOs
 * and returns the computed rate as JSON.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RateController {

    private final RateCalculationAppService appService;

    @Autowired
    public RateController(RateCalculationAppService appService) {
        this.appService = appService;
    }

    @GetMapping("/rate")
    public ResponseEntity<RateResponse> calculateRate(
        @RequestParam("callerNumber") String callerNumber,
        @RequestParam("calleeNumber") String calleeNumber,
        @RequestParam("customerType") String customerType,
        @RequestParam("callStartTime")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime callStartTime
    ) {
        CallContextRequest request = new CallContextRequest(
            callerNumber, calleeNumber, customerType, callStartTime
        );
        RateResponse response = appService.calculateRate(request);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleError(Exception e) {
        return ResponseEntity.badRequest()
            .body(new ErrorResponse("INVALID_REQUEST", e.getMessage()));
    }

    public record ErrorResponse(String code, String message) {}
}
