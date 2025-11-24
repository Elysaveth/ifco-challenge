package com.ifco.challenge.application.command;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ifco.challenge.application.bus.EventBus;
import com.ifco.challenge.application.dto.TelemetryEventDTO;
import com.ifco.challenge.application.usecases.RecordTelemetryUseCase;
import com.ifco.challenge.domain.checks.TelemetryAnalyzer;
import com.ifco.challenge.domain.exception.DuplicateRecordException;
import com.ifco.challenge.domain.model.Telemetry;
import com.ifco.challenge.domain.repository.TelemetryRepo;

@Component
public class RecordTelemetryCommandHandler implements RecordTelemetryUseCase {
    
    private final TelemetryRepo telemetryRepo;
    private final EventBus eventBus;
    private final TelemetryAnalyzer telemetryAnalyzer;


    public RecordTelemetryCommandHandler (
        TelemetryRepo telemetryRepo,
        EventBus eventBus,
        TelemetryAnalyzer telemetryAnalyzer) {
            this.telemetryRepo = telemetryRepo;
            this.eventBus = eventBus;
            this.telemetryAnalyzer = telemetryAnalyzer;
    }

    public void handle(RecordTelemetryCommand command) {
        Telemetry telemetry = new Telemetry(
            command.deviceId(),
            (command.temperature() == null) ? (double) 0 : command.temperature(),
            command.date()
        );

        List<Telemetry> storedTelemetry = telemetryRepo.findByDate(telemetry.date());
        
        try {
            if (!telemetryAnalyzer.isRepeatedEvent(telemetry, storedTelemetry)) {
                Telemetry saved = telemetryRepo.save(telemetry);

                // TODO Handle retries and dead-letter
                eventBus.publish(new TelemetryEventDTO(
                    saved.deviceId(),
                    saved.temperature(),
                    saved.date()
        
                ));
            }
        } catch (DuplicateRecordException e) {
            e.printStackTrace();
            telemetryRepo.deleteDuplicate(telemetry);
        }
    }

}
