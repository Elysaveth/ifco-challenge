package com.ifco.challenge.application.event.listener;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Component;

import com.ifco.challenge.application.service.TelemetryProjectionService;
import com.ifco.challenge.domain.logic.TelemetryDomainService;
import com.ifco.challenge.domain.model.Telemetry;


@Component
public class TelemetryEventHandler {
    
    private final TelemetryProjectionService telemetryProjectionService;
    private final TelemetryDomainService telemetryDomainService;

    public TelemetryEventHandler (
        TelemetryProjectionService telemetryProjectionService,
            TelemetryDomainService telemetryDomainService) {
        this.telemetryProjectionService = telemetryProjectionService;
        this.telemetryDomainService = telemetryDomainService;
    }

    public void handle(Telemetry event) throws InterruptedException, ExecutionException {

        Optional<Telemetry> latestRecorded = telemetryProjectionService.getLatestTelemetry(event.deviceId()).get();
        
        if (telemetryDomainService.isLastEvent(event, latestRecorded)) {
            telemetryProjectionService.save(event);
        }
    }
}
