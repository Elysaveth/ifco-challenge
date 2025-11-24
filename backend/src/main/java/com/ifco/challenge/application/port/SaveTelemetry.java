package com.ifco.challenge.application.port;

import com.ifco.challenge.domain.model.Telemetry;

public interface SaveTelemetry {
    Telemetry save(Telemetry telemetry);
}
