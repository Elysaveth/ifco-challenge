package com.ifco.challenge.application.command;

import org.springframework.stereotype.Component;

import com.ifco.challenge.domain.bus.EventBus;
import com.ifco.challenge.domain.event.TelemetryRecorded;
import com.ifco.challenge.domain.model.Telemetry;
import com.ifco.challenge.domain.repository.TelemetryRepo;

@Component
public class RecordTelemetryCommandHandler {
    
    private final TelemetryRepo telemetryRepo;
    private final EventBus eventBus;

    public RecordTelemetryCommandHandler (
        TelemetryRepo telemetryRepo,
        EventBus eventBus) {
            this.telemetryRepo = telemetryRepo;
            this.eventBus = eventBus;
    }

    public void handle(RecordTelemetryCommand command) {
        Telemetry telemetry = new Telemetry(
            command.deviceId(),
            (command.temperature() == null) ? (double) 0 : command.temperature(),
            command.timestamp()
        );

        Telemetry saved = telemetryRepo.save(telemetry);

        eventBus.publish(new TelemetryRecorded(
            saved.getDeviceId(),
            saved.getTemperature(),
            saved.getTimestamp()
        ));
    }

}
