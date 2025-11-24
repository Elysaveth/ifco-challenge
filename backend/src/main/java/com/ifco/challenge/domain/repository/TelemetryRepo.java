package com.ifco.challenge.domain.repository;

import java.time.Instant;
import java.util.List;

import com.ifco.challenge.domain.model.Telemetry;

public interface TelemetryRepo {

    Telemetry save(Telemetry telemetry);

    List<Telemetry> findByDate(Instant date);
    
    void deleteDuplicate(Telemetry telemetry);
}