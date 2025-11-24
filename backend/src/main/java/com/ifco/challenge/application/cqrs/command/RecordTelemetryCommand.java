package com.ifco.challenge.application.cqrs.command;

import java.time.Instant;

// TODO Does this belong in domain?
public record RecordTelemetryCommand (
    String deviceId,
    Double temperature,
    Instant date
) {}
