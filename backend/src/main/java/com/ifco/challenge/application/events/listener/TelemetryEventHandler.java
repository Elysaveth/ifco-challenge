package com.ifco.challenge.application.events.listener;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Component;

import com.ifco.challenge.application.service.TelemetryProjectionService;
import com.ifco.challenge.domain.logic.TelemetryLogic;
import com.ifco.challenge.domain.model.Telemetry;


@Component
public class TelemetryEventHandler {
    
    private final TelemetryProjectionService telemetryProjectionService;
    private final TelemetryLogic telemetryLogic;

    public TelemetryEventHandler (
        TelemetryProjectionService telemetryProjectionService,
            TelemetryLogic telemetryLogic) {
        this.telemetryProjectionService = telemetryProjectionService;
        this.telemetryLogic = telemetryLogic;
    }

    public void handle(Telemetry event) throws InterruptedException, ExecutionException {

        Optional<Telemetry> latestRecorded = telemetryProjectionService.getLatestTelemetry(event.deviceId()).get();
        
        if (telemetryLogic.isLastEvent(event, latestRecorded)) {
            telemetryProjectionService.save(event);
        }
    }
}
