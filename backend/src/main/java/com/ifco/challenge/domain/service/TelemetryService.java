package com.ifco.challenge.domain.service;

import com.ifco.challenge.domain.model.Telemetry;

import java.util.List;
import java.util.Optional;

public interface TelemetryService {

    public List<Telemetry> getAllTelemetry();

    public Optional<Telemetry> getByTelemetryId(long id);

    public void recordTelemetry(Telemetry telemetry);
}
