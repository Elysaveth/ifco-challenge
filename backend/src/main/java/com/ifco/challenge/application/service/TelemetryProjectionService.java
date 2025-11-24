package com.ifco.challenge.application.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import com.ifco.challenge.application.port.GetAllTelemetryProjection;
import com.ifco.challenge.application.port.GetTelemetryProjection;
import com.ifco.challenge.application.port.SaveTelemetryProjection;
import com.ifco.challenge.domain.model.Telemetry;
import com.ifco.challenge.domain.repository.TelemetryProjectionRepo;

@Service
public class TelemetryProjectionService implements
        GetTelemetryProjection,
        SaveTelemetryProjection,
        GetAllTelemetryProjection {

    private final TelemetryProjectionRepo repo;

    public TelemetryProjectionService(TelemetryProjectionRepo repo) {
        this.repo = repo;
    }

    @Override
    public CompletableFuture<Optional<Telemetry>> getLatestTelemetry(String deviceId) {
        return repo.findByDeviceId(deviceId);
    }

    @Override
    public CompletableFuture<Void> save(Telemetry telemetry) {
        return repo.save(telemetry);
    }

    @Override
    public CompletableFuture<List<Telemetry>> getAll() {
        return repo.findAll();
    }
}