package com.ifco.challenge.infrastructure.cache;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Repository;

import com.ifco.challenge.domain.model.Telemetry;
import com.ifco.challenge.infrastructure.cache.entity.TelemetryProjectionEntity;

import io.lettuce.core.ScanArgs;
import io.lettuce.core.ScanCursor;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Repository
public class RedisTelemetryProjectionRepo {
    
    private final RedisAsyncCommands<String, String> async;

    public RedisTelemetryProjectionRepo (StatefulRedisConnection<String, String> lettuceConnection) {
        this.async = lettuceConnection.async();
    }

    public CompletableFuture<Void> save(Telemetry telemetry) {
        log.info("Saving telemetry: " + telemetry);
        Map<String, String> map = Map.of(
            "temperature", String.valueOf(telemetry.temperature()),
            "date", telemetry.date().toString()
        );

        // TODO Create proper monitoring
        log.info("Initiating async save for device: " + telemetry.deviceId());
        return async.hmset(telemetry.deviceId(), map).toCompletableFuture().thenApply(result -> null);
    }

    public CompletableFuture<List<TelemetryProjectionEntity>> findAllAsync() {
        List<TelemetryProjectionEntity> result = new ArrayList<>();
        ScanArgs scanArgs = ScanArgs.Builder.matches("*").limit(500);
        return scanRecursive(ScanCursor.INITIAL, scanArgs, result);
    }

    public CompletableFuture<Optional<TelemetryProjectionEntity>> findByDeviceId(String deviceId) {
        
        return async.hgetall(deviceId).toCompletableFuture().thenApply(map -> {
            if (map == null || map.isEmpty()) return Optional.empty();
            return Optional.of(mapToProjection(deviceId, map));
        });
    }

    private CompletableFuture<List<TelemetryProjectionEntity>> scanRecursive(
        ScanCursor cursor, ScanArgs scanArgs, List<TelemetryProjectionEntity> result) {

    return async.scan(cursor, scanArgs)
            .toCompletableFuture()
            .thenCompose(scanCursor -> {
                List<CompletableFuture<Void>> readFutures = new ArrayList<>();

                for (String key : scanCursor.getKeys()) {
                    CompletableFuture<Void> f = async.hgetall(key)
                            .toCompletableFuture()
                            .thenAccept(map -> {
                                if (!map.isEmpty()) {
                                    result.add(mapToProjection(key, map));
                                }
                            });
                    readFutures.add(f);
                }

                CompletableFuture<Void> allReads = CompletableFuture.allOf(readFutures.toArray(new CompletableFuture[0]));

                return allReads.thenCompose(v -> {
                    if (!cursor.isFinished()) {
                        return CompletableFuture.completedFuture(result);
                    } else {
                        return scanRecursive(scanCursor, scanArgs, result);
                    }
                });
            });
    }

    private TelemetryProjectionEntity mapToProjection(String deviceId, Map<String, String> map) {
        return new TelemetryProjectionEntity(
                deviceId,
                (double) Double.parseDouble((String) map.get("temperature")),
                Instant.parse((String) map.get("date"))
        );
    }
}