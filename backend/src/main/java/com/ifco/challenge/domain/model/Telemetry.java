package com.ifco.challenge.domain.model;

import java.time.Instant;

public record Telemetry (
    String deviceId,
    double temperature,
    Instant date
) {}
