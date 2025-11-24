package com.ifco.challenge.application.events;

import com.ifco.challenge.application.dto.TelemetryEventDTO;

public interface EventBus {
    void publish(TelemetryEventDTO event);
}
