package com.ifco.challenge.infrastructure.cache;

import java.time.Instant;

import com.ifco.challenge.domain.model.Telemetry;

public record DeviceTelemetryProjection (
    String deviceId,
    double temperature,
    Instant date
) {
    public static Telemetry toDomain(DeviceTelemetryProjection projection) {
        return new Telemetry(
            projection.deviceId,
            projection.temperature,
            projection.date
        );
    }
}
