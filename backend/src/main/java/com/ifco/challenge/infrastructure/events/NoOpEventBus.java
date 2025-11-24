package com.ifco.challenge.infrastructure.events;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.ifco.challenge.application.bus.EventBus;
import com.ifco.challenge.application.dto.TelemetryEventDTO;

@Component
@Profile("test")
public class NoOpEventBus implements EventBus {

    @Override
    public void publish(TelemetryEventDTO event) {}
}
