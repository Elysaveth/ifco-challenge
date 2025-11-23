package com.ifco.challenge.infrastructure.cache;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Repository;

import com.ifco.challenge.domain.model.Telemetry;

import io.lettuce.core.ScanArgs;
import io.lettuce.core.ScanCursor;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;

@Repository
public class TelemetryProjectionRepo {
    
    private final RedisAsyncCommands<String, String> async;

    public TelemetryProjectionRepo (StatefulRedisConnection<String, String> lettuceConnection) {
        this.async = lettuceConnection.async();
    }

    public CompletableFuture<Void> save(Telemetry telemetry) {
        Map<String, String> map = Map.of(
            "temperature", String.valueOf(telemetry.temperature()),
            "date", telemetry.date().toString()
        );

        return async.hmset(telemetry.deviceId(), map).toCompletableFuture().thenApply(result -> null);
    }

    public CompletableFuture<List<DeviceTelemetryProjection>> findAllAsync() {
        List<DeviceTelemetryProjection> result = new ArrayList<>();
        ScanArgs scanArgs = ScanArgs.Builder.matches("*").limit(500);
        return scanRecursive(ScanCursor.INITIAL, scanArgs, result);
    }

    private CompletableFuture<List<DeviceTelemetryProjection>> scanRecursive(
        ScanCursor cursor, ScanArgs scanArgs, List<DeviceTelemetryProjection> result) {

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

    public CompletableFuture<Optional<DeviceTelemetryProjection>> findByDeviceId(String deviceId) {
        
        return async.hgetall(deviceId).toCompletableFuture().thenApply(map -> {
            if (map == null || map.isEmpty()) return Optional.empty();
            return Optional.of(mapToProjection(deviceId, map));
        });
    }

    private DeviceTelemetryProjection mapToProjection(String deviceId, Map<String, String> map) {
        return new DeviceTelemetryProjection(
                deviceId,
                (double) Double.parseDouble((String) map.get("temperature")),
                Instant.parse((String) map.get("date"))
        );
    }
}