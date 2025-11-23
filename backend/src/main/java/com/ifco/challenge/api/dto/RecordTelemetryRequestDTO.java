package com.ifco.challenge.api.dto;

import java.time.Instant;

public record RecordTelemetryRequestDTO (
    String deviceId,
    Double temperature,
    Instant date
) {}
