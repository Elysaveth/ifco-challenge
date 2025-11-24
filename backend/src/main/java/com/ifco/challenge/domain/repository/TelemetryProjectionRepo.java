package com.ifco.challenge.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.ifco.challenge.domain.model.Telemetry;

public interface TelemetryProjectionRepo {

    CompletableFuture<Void> save(Telemetry telemetry);

    CompletableFuture<Optional<Telemetry>> findByDeviceId(String deviceId);

    CompletableFuture<List<Telemetry>> findAll();
}
