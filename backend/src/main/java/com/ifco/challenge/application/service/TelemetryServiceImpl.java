package com.ifco.challenge.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ifco.challenge.domain.model.Telemetry;
import com.ifco.challenge.domain.repository.TelemetryRepository;
import com.ifco.challenge.domain.service.TelemetryService;

@Service
public class TelemetryServiceImpl implements TelemetryService {

    private final TelemetryRepository telemetryRepository;

    public TelemetryServiceImpl(TelemetryRepository telemetryRepository) {
        this.telemetryRepository = telemetryRepository;
    }

    public List<Telemetry> getAllTelemetry() {
        return telemetryRepository.findAll();
    }

    public Optional<Telemetry> getByTelemetryId(long id) {
        return telemetryRepository.findById(id);
    }

    public void recordTelemetry(Telemetry telemetry) {
        telemetryRepository.save(telemetry);
    }
}
