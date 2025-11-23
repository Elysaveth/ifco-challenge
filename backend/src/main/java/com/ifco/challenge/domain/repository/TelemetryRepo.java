package com.ifco.challenge.domain.repository;

import com.ifco.challenge.domain.model.Telemetry;

public interface TelemetryRepo {
    Telemetry save(Telemetry telemetry);
}