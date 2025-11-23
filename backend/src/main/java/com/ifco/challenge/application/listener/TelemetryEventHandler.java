package com.ifco.challenge.application.listener;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import com.ifco.challenge.application.port.GetLatestTelemetry;
import com.ifco.challenge.application.port.SaveLatestTelemetry;
import com.ifco.challenge.domain.event.TelemetryAnalyzer;
import com.ifco.challenge.domain.model.Telemetry;

public class TelemetryEventHandler {
    
    private final GetLatestTelemetry getLatestTelemetry;
    private final SaveLatestTelemetry saveLatestTelemetry;
    private final TelemetryAnalyzer telemetryAnalyzer;

    public TelemetryEventHandler (
            GetLatestTelemetry getLatestTelemetry,
            SaveLatestTelemetry saveLatestTelemetry,
            TelemetryAnalyzer telemetryAnalyzer) {
        this.getLatestTelemetry = getLatestTelemetry;
        this.saveLatestTelemetry = saveLatestTelemetry;
        this.telemetryAnalyzer = telemetryAnalyzer;
    }

    public void handle(Telemetry event) throws InterruptedException, ExecutionException {

        Optional<Telemetry> latestRecorded = getLatestTelemetry.getLatestTelemetry(event.deviceId()).get();
        
        if (telemetryAnalyzer.isLastEvent(event, latestRecorded)) {
            saveLatestTelemetry.save(event);
        }
    }
}
