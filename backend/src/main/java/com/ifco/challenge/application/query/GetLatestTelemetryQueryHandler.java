package com.ifco.challenge.application.query;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Component;

import com.ifco.challenge.application.port.GetAllLatestTelemetry;
import com.ifco.challenge.application.usecases.GetLatestTelemetryUseCase;
import com.ifco.challenge.domain.model.Telemetry;

@Component
public class GetLatestTelemetryQueryHandler implements GetLatestTelemetryUseCase {

    private final GetAllLatestTelemetry getAllLatestTelemetry;

    public GetLatestTelemetryQueryHandler(GetAllLatestTelemetry getAllLatestTelemetry) {
        this.getAllLatestTelemetry = getAllLatestTelemetry;
    }

    @Override
    public List<Telemetry> handle() throws InterruptedException, ExecutionException {
        return getAllLatestTelemetry.getAll().get();
    }
    
}
