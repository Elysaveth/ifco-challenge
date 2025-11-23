package com.ifco.challenge.infrastructure.events;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.ifco.challenge.domain.bus.EventBus;
import com.ifco.challenge.domain.event.TelemetryRecorded;

@Component
@Profile("test")
public class NoOpEventBus implements EventBus {

    @Override
    public void publish(TelemetryRecorded event) {}
}
