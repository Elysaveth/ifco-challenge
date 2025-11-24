package com.ifco.challenge.application.cqrs.command;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ifco.challenge.application.dto.TelemetryEventDTO;
import com.ifco.challenge.application.events.EventBus;
import com.ifco.challenge.application.service.TelemetryService;
import com.ifco.challenge.application.usecases.RecordTelemetryUseCase;
import com.ifco.challenge.domain.exception.DuplicateRecordException;
import com.ifco.challenge.domain.logic.TelemetryLogic;
import com.ifco.challenge.domain.model.Telemetry;

@Component
public class RecordTelemetryCommandHandler implements RecordTelemetryUseCase {
    
    private final EventBus eventBus;
    private final TelemetryLogic telemetryAnalyzer;
    private final TelemetryService telemetryService;


    public RecordTelemetryCommandHandler (
        EventBus eventBus,
        TelemetryLogic telemetryAnalyzer,
        TelemetryService telemetryService) {
            this.eventBus = eventBus;
            this.telemetryAnalyzer = telemetryAnalyzer;
            this.telemetryService = telemetryService;
    }

    public void handle(RecordTelemetryCommand command) {
        Telemetry telemetry = new Telemetry(
            command.deviceId(),
            (command.temperature() == null) ? (double) 0 : command.temperature(),
            command.date()
        );

        List<Telemetry> storedTelemetry = telemetryService.findByDate(telemetry.date());
        
        try {
            if (!telemetryAnalyzer.isRepeatedEvent(telemetry, storedTelemetry)) {
                Telemetry saved = telemetryService.save(telemetry);

                // TODO Handle retries and dead-letter
                eventBus.publish(new TelemetryEventDTO(
                    saved.deviceId(),
                    saved.temperature(),
                    saved.date()
        
                ));
            }
            
        } catch (DuplicateRecordException e) {
            e.printStackTrace();
            telemetryService.deleteDuplicate(telemetry);
        }
    }

}
