package com.ifco.challenge.application.port;

import java.util.concurrent.CompletableFuture;

import com.ifco.challenge.domain.model.Telemetry;

public interface SaveLatestTelemetry {
    CompletableFuture<Void> save(Telemetry telemetry);
}
