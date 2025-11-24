package com.ifco.challenge.api;

import com.ifco.challenge.api.dto.RecordTelemetryRequestDTO;
import com.ifco.challenge.application.cqrs.command.RecordTelemetryCommand;
import com.ifco.challenge.application.usecases.GetLatestTelemetryUseCase;
import com.ifco.challenge.application.usecases.RecordTelemetryUseCase;
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

    private final RecordTelemetryUseCase recordHandler;
    private final GetLatestTelemetryUseCase getLatestHandler;

    public TelemetryController(
            RecordTelemetryUseCase recordHandler,
            GetLatestTelemetryUseCase getLatestHandler) {
        this.recordHandler = recordHandler;
        this.getLatestHandler = getLatestHandler;
    }

    @PostMapping
    public ResponseEntity<Void> recordTelemetry(@RequestBody RecordTelemetryRequestDTO telemetry) {

        // Maybe just DTO directly
        RecordTelemetryCommand command = new RecordTelemetryCommand(
            telemetry.deviceId(),
            telemetry.measurement(),
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
