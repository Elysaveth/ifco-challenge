package com.ifco.challenge.application.port;

import java.time.Instant;
import java.util.List;

import com.ifco.challenge.domain.model.Telemetry;

public interface GetTelemetry {
    List<Telemetry> findByDate(Instant date);
}
