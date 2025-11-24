package com.ifco.challenge.infrastructure.cache;

import com.ifco.challenge.domain.model.Telemetry;
import com.ifco.challenge.domain.repository.TelemetryProjectionRepo;
import com.ifco.challenge.infrastructure.cache.entity.TelemetryProjectionEntity;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;

@Component
public class TelemetryProjectionRepoImpl implements TelemetryProjectionRepo {

    private final RedisTelemetryProjectionRepo projectionRepo;

    public TelemetryProjectionRepoImpl(RedisTelemetryProjectionRepo projectionRepo) {
        this.projectionRepo = projectionRepo;
    }

    @Override
    public CompletableFuture<Optional<Telemetry>> findByDeviceId(String deviceId) {
        return projectionRepo.findByDeviceId(deviceId)
                .thenApply(result -> result.map(TelemetryProjectionEntity::toDomain));
    }

    @Override
    public CompletableFuture<Void> save(Telemetry telemetry) {
        return projectionRepo.save(telemetry);
    }

    @Override
    public CompletableFuture<List<Telemetry>> findAll() {
        return projectionRepo.findAllAsync()
                .thenApply(TelemetryProjectionEntity::toDomain);
    }
}
