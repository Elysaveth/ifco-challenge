package com.ifco.challenge.application.listener;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Component;

import com.ifco.challenge.domain.checks.TelemetryAnalyzer;
import com.ifco.challenge.domain.model.Telemetry;
import com.ifco.challenge.domain.repository.TelemetryProjectionRepo;


@Component
public class TelemetryEventHandler {
    
    private final TelemetryProjectionRepo telemetryProjectionRepo;
    private final TelemetryAnalyzer telemetryAnalyzer;

    public TelemetryEventHandler (
        TelemetryProjectionRepo telemetryProjectionRepo,
            TelemetryAnalyzer telemetryAnalyzer) {
        this.telemetryProjectionRepo = telemetryProjectionRepo;
        this.telemetryAnalyzer = telemetryAnalyzer;
    }

    public void handle(Telemetry event) throws InterruptedException, ExecutionException {

        Optional<Telemetry> latestRecorded = telemetryProjectionRepo.findByDeviceId(event.deviceId()).get();
        
        if (telemetryAnalyzer.isLastEvent(event, latestRecorded)) {
            telemetryProjectionRepo.save(event);
        }
    }
}
