package com.ifco.challenge.application.port;

import java.util.concurrent.CompletableFuture;

import com.ifco.challenge.domain.model.Telemetry;

public interface SaveTelemetryProjection {
    CompletableFuture<Void> save(Telemetry telemetry);
}
