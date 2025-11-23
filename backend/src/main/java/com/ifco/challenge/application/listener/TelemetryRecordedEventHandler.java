package com.ifco.challenge.application.listener;

import com.ifco.challenge.domain.event.TelemetryRecorded;
import com.ifco.challenge.infrastructure.cache.TelemetryProjectionRepo;

public class TelemetryRecordedEventHandler {
    
    private final TelemetryProjectionRepo projectionRepo;

    public TelemetryRecordedEventHandler (TelemetryProjectionRepo projectionRepo) {
        this.projectionRepo = projectionRepo;
    }

    public void handle (TelemetryRecorded event) {
        
        // TODO calculate if event is last

        projectionRepo.updateLatestTemperatures(
            event.deviceId(),
            event.temperature(),
            event.timestamp()
        );
    }
}
