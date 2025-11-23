package com.ifco.challenge.application.usecases;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.ifco.challenge.domain.model.Telemetry;

public interface GetLatestTelemetryUseCase {
    List<Telemetry> handle() throws InterruptedException, ExecutionException;
}
