package com.ifco.challenge.application.dto;

import java.time.Instant;

public record TelemetryEventDTO(
        String deviceId,
        Double temperature,
        Instant date
) {}
