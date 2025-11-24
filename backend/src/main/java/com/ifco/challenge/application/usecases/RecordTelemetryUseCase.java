package com.ifco.challenge.application.usecases;

import com.ifco.challenge.application.cqrs.command.RecordTelemetryCommand;

public interface RecordTelemetryUseCase {
    void handle(RecordTelemetryCommand command);
}
