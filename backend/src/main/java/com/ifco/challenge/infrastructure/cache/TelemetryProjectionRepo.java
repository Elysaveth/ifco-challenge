package com.ifco.challenge.infrastructure.cache;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Repository;

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

    public void updateLatestTemperatures(String deviceId, double temperature, Instant timestamp) {
        // TODO update only if is last

        Map<String, String> map = Map.of(
            "temperature", String.valueOf(temperature),
            "date", timestamp.toString()
        );

        async.hmset(deviceId, map).toCompletableFuture();
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

                // Continue scanning if not finished
                return allReads.thenCompose(v -> {
                    if (!cursor.isFinished()) {
                        return CompletableFuture.completedFuture(result);
                    } else {
                        return scanRecursive(scanCursor, scanArgs, result);
                    }
                });
            });
    }

    // Added for convenince
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