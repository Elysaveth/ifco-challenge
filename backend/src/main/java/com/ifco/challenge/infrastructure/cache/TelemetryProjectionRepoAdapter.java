package com.ifco.challenge.infrastructure.cache;

import com.ifco.challenge.domain.model.Telemetry;
import com.ifco.challenge.application.port.GetAllLatestTelemetry;
import com.ifco.challenge.application.port.GetLatestTelemetry;
import com.ifco.challenge.application.port.SaveLatestTelemetry;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;

@Component
public class TelemetryProjectionRepoAdapter implements GetLatestTelemetry, SaveLatestTelemetry, GetAllLatestTelemetry {

    private final TelemetryProjectionRepo projectionRepo;

    public TelemetryProjectionRepoAdapter(TelemetryProjectionRepo projectionRepo) {
        this.projectionRepo = projectionRepo;
    }

    @Override
    public CompletableFuture<Optional<Telemetry>> getLatestTelemetry(String deviceId) {
        return projectionRepo.findByDeviceId(deviceId)
                .thenApply(result -> result.map(DeviceTelemetryProjection::toDomain));
    }

    @Override
    public CompletableFuture<Void> save(Telemetry telemetry) {
        return projectionRepo.save(telemetry);
    }

    @Override
    public CompletableFuture<List<Telemetry>> getAll() {
        return projectionRepo.findAllAsync()
                .thenApply(DeviceTelemetryProjection::toDomain);
    }
}
