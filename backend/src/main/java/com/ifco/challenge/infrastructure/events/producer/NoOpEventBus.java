package com.ifco.challenge.infrastructure.events.producer;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.ifco.challenge.application.dto.TelemetryEventDTO;
import com.ifco.challenge.application.event.EventBus;

@Component
@Profile("test")
public class NoOpEventBus implements EventBus {

    @Override
    public void publish(TelemetryEventDTO event) {}
}
