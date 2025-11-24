package com.ifco.challenge.infrastructure.cache.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.ifco.challenge.domain.model.Telemetry;

public record TelemetryProjectionEntity (
    String deviceId,
    double temperature,
    Instant date
) {
    public static Telemetry toDomain(TelemetryProjectionEntity projection) {
        return new Telemetry(
            projection.deviceId,
            projection.temperature,
            projection.date
        );
    }

    public static List<Telemetry> toDomain(List<TelemetryProjectionEntity> projection) {
        int size = projection.size();
        var list = new ArrayList<Telemetry>(size);

        for (int i=0; i<size; i++) {
            var p = projection.get(i);
            list.add(new Telemetry(
                p.deviceId,
                p.temperature,
                p.date
            ));
        }

        return list;
    }
}
