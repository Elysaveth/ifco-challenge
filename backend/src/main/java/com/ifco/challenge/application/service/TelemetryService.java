package com.ifco.challenge.application.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ifco.challenge.application.port.DeleteDuplicateTelemetry;
import com.ifco.challenge.application.port.GetTelemetry;
import com.ifco.challenge.application.port.SaveTelemetry;
import com.ifco.challenge.domain.model.Telemetry;
import com.ifco.challenge.domain.repository.TelemetryRepo;

@Service
public class TelemetryService implements SaveTelemetry, DeleteDuplicateTelemetry, GetTelemetry {

    private final TelemetryRepo repo;

    public TelemetryService(TelemetryRepo repo) {
        this.repo = repo;
    }

    @Override
    public Telemetry save(Telemetry telemetry) {
        return repo.save(telemetry);
    }

    @Override
    public List<Telemetry> findByDate(Instant date) {
        return repo.findByDate(date);
    }

    @Override
    public void deleteDuplicate(Telemetry telemetry) {
        repo.deleteDuplicate(telemetry);
    }
}