package com.ifco.challenge.api;

import com.ifco.challenge.api.dto.RecordTelemetryRequestDTO;
import com.ifco.challenge.application.command.RecordTelemetryCommand;
import com.ifco.challenge.application.command.RecordTelemetryCommandHandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/telemetry")
public class TelemetryController {

    private final RecordTelemetryCommandHandler handler;

    public TelemetryController(RecordTelemetryCommandHandler handler) {
        this.handler = handler;
    }

    @PostMapping
    public ResponseEntity<Void> recordTelemetry(@RequestBody RecordTelemetryRequestDTO telemetry) {

        RecordTelemetryCommand command = new RecordTelemetryCommand(
            telemetry.deviceId(),
            telemetry.temperature(),
            telemetry.date()
        );

        handler.handle(command);

        return ResponseEntity.accepted().build();
    }
}
