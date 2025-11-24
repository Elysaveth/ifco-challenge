package com.ifco.challenge.application.event;

import com.ifco.challenge.application.dto.TelemetryEventDTO;

public interface EventBus {
    void publish(TelemetryEventDTO event);
}
