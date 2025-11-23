package com.ifco.challenge.domain.repository;

import java.util.List;
import java.util.Optional;

import com.ifco.challenge.domain.model.Telemetry;

public interface TelemetryRepository {
    Optional<Telemetry> findById(Long id);
    Telemetry save(Telemetry telemetry);
    List<Telemetry> findAll();
    void delete(Telemetry telemetry);
}