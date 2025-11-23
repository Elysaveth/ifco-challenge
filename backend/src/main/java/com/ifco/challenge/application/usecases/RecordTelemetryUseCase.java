package com.ifco.challenge.application.usecases;

import com.ifco.challenge.application.command.RecordTelemetryCommand;

public interface RecordTelemetryUseCase {
    void handle(RecordTelemetryCommand command);
}
