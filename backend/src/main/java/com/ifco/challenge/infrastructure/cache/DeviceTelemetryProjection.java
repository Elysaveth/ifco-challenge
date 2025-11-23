package com.ifco.challenge.infrastructure.cache;

import java.time.Instant;

public record DeviceTelemetryProjection (
    String deviceId,
    double temperature,
    Instant timestamp
) {}
