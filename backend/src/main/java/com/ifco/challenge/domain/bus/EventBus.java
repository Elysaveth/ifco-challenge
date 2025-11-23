package com.ifco.challenge.domain.bus;

import com.ifco.challenge.domain.event.TelemetryRecorded;

public interface EventBus {
    void publish(TelemetryRecorded event);
}
