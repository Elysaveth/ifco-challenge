package com.ifco.challenge.domain.bus;

import com.ifco.challenge.domain.model.Telemetry;

public interface EventBus {
    void publish(Telemetry event);
}
