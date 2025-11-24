package com.ifco.challenge.domain.checks;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ifco.challenge.domain.exception.DuplicateRecordException;
import com.ifco.challenge.domain.model.Telemetry;

@Component
public class TelemetryAnalyzer {
    public boolean isLastEvent(Telemetry event, Optional<Telemetry> lastStoredEvent) {

        if (lastStoredEvent.isPresent() && lastStoredEvent.get().date().isAfter(event.date())) {
            return false;
        }

        return true;
    }

    public boolean isRepeatedEvent(Telemetry event, List<Telemetry> storedEvent) throws DuplicateRecordException {
        if (storedEvent.isEmpty()) {
            return false;
        }

        if (storedEvent.size() > 1) {
            throw new DuplicateRecordException();
        }

        return true;
    }
}
