package com.ifco.challenge.api;

import com.ifco.challenge.api.dto.RecordTelemetryRequestDTO;
import com.ifco.challenge.application.command.RecordTelemetryCommand;
import com.ifco.challenge.application.command.RecordTelemetryCommandHandler;
import com.ifco.challenge.application.query.GetLatestTelemetryQueryHandler;
import com.ifco.challenge.domain.model.Telemetry;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/telemetry")
public class TelemetryController {

    private final RecordTelemetryCommandHandler recordHandler;
    private final GetLatestTelemetryQueryHandler getLatestHandler;

    public TelemetryController(
            RecordTelemetryCommandHandler recordHandler,
            GetLatestTelemetryQueryHandler getLatestHandler) {
        this.recordHandler = recordHandler;
        this.getLatestHandler = getLatestHandler;
    }

    @PostMapping
    public ResponseEntity<Void> recordTelemetry(@RequestBody RecordTelemetryRequestDTO telemetry) {

        // Maybe just DTO directly
        RecordTelemetryCommand command = new RecordTelemetryCommand(
            telemetry.deviceId(),
            telemetry.temperature(),
            telemetry.date()
        );

        recordHandler.handle(command);

        return ResponseEntity.accepted().build();
    }

    @GetMapping("/getLatest")
    public List<Telemetry> getLatestForAllDevices() throws InterruptedException, ExecutionException {
        return getLatestHandler.handle();
    }
    
}
