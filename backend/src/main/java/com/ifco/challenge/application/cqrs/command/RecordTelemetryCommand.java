package com.ifco.challenge.application.cqrs.command;

import java.time.Instant;

public record RecordTelemetryCommand (
    String deviceId,
    Double temperature,
    Instant date
) {}
