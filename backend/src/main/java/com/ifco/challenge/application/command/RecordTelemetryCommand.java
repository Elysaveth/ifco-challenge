package com.ifco.challenge.application.command;

import java.time.Instant;

public record RecordTelemetryCommand (
    String deviceId,
    Double temperature,
    Instant date
) {}
