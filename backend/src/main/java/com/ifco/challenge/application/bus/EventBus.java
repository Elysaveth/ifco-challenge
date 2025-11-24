package com.ifco.challenge.application.bus;

import com.ifco.challenge.application.dto.TelemetryEventDTO;

public interface EventBus {
    void publish(TelemetryEventDTO event);
}
