package com.ifco.challenge.application.cqrs.query;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Component;

import com.ifco.challenge.application.service.TelemetryProjectionService;
import com.ifco.challenge.application.usecases.GetLatestTelemetryUseCase;
import com.ifco.challenge.domain.model.Telemetry;

@Component
public class GetLatestTelemetryQueryHandler implements GetLatestTelemetryUseCase {

    private final TelemetryProjectionService telemetryProjectionService;

    public GetLatestTelemetryQueryHandler(TelemetryProjectionService telemetryProjectionService) {
        this.telemetryProjectionService = telemetryProjectionService;
    }

    @Override
    public List<Telemetry> handle() throws InterruptedException, ExecutionException {
        return telemetryProjectionService.getAll().get();
    }
    
}
