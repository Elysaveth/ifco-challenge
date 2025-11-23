package com.ifco.challenge.domain.event;

import java.util.Optional;

import com.ifco.challenge.domain.model.Telemetry;

public class TelemetryAnalyzer {
    public boolean isLastEvent(Telemetry event, Optional<Telemetry> lastStoredEvent) {

        if (lastStoredEvent.isPresent() && lastStoredEvent.get().date().isAfter(event.date())) {
            return false;
        }

        return true;
    }
}
