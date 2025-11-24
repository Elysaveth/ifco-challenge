package com.ifco.challenge.application.port;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.ifco.challenge.domain.model.Telemetry;

public interface GetTelemetryProjection {
    CompletableFuture<Optional<Telemetry>> getLatestTelemetry(String deviceId);
}
