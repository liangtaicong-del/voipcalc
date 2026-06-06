package com.voip.calc.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Application-layer DTO for rate calculation request input.
 * Converted from interface-layer DTO by the controller.
 */
public record CallContextRequest(
    @NotBlank(message = "callerNumber is required")
    String callerNumber,

    @NotBlank(message = "calleeNumber is required")
    String calleeNumber,

    @NotBlank(message = "customerType is required")
    String customerType,

    @NotNull(message = "callStartTime is required")
    LocalDateTime callStartTime
) {}
