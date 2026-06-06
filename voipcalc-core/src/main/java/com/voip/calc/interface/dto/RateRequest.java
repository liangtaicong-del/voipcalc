package com.voip.calc.interface.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Interface-layer DTO for incoming rate calculation request.
 * Validates required query parameters from the REST endpoint.
 */
public record RateRequest(
    @NotBlank(message = "callerNumber is required")
    String callerNumber,

    @NotBlank(message = "calleeNumber is required")
    String calleeNumber,

    @NotBlank(message = "customerType is required")
    String customerType,

    @NotBlank(message = "callStartTime is required")
    String callStartTime
) {}
