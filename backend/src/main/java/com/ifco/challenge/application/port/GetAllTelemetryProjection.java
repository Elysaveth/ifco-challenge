package com.ifco.challenge.application.port;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.ifco.challenge.domain.model.Telemetry;

public interface GetAllTelemetryProjection {
    CompletableFuture<List<Telemetry>> getAll();
}

