package com.ifco.challenge.domain.event;

import java.time.Instant;

public record TelemetryRecorded (
    String deviceId,
    Double temperature,
    Instant timestamp
) {}
