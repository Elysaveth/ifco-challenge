package com.ifco.challenge.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ifco.challenge.domain.exception.DuplicateRecordException;
import com.ifco.challenge.domain.logic.TelemetryDomainService;
import com.ifco.challenge.domain.model.Telemetry;

class TelemetryDomainServiceTest {

    private TelemetryDomainService telemetryDomainService;

    @BeforeEach
    void setUp() {
        telemetryDomainService = new TelemetryDomainService();
    }

    @Test
    void isLastEvent_whenLastIsAfterEvent_returnsFalse() {
        Instant now = Instant.now();
        Telemetry older = new Telemetry("d1", 10.0, now.minusSeconds(60));
        Telemetry event = new Telemetry("d1", 11.0, now);

        // event is newer than older
        assertTrue(telemetryDomainService.isLastEvent(event, Optional.of(older)));
        // older compared against newer returns false (older is not last)
        assertFalse(telemetryDomainService.isLastEvent(older, Optional.of(event)));
    }

    @Test
    void isLastEvent_whenNoStored_returnsTrue() {
        Instant now = Instant.now();
        Telemetry event = new Telemetry("d2", 5.0, now);

        assertTrue(telemetryDomainService.isLastEvent(event, Optional.empty()));
    }

    @Test
    void isRepeatedEvent_whenNoStored_returnsFalse() throws DuplicateRecordException {
        Telemetry event = new Telemetry("d3", 8.0, Instant.now());
        assertFalse(telemetryDomainService.isRepeatedEvent(event, List.of()));
    }

    @Test
    void isRepeatedEvent_whenSingleStored_returnsTrue() throws DuplicateRecordException {
        Instant now = Instant.now();
        Telemetry event = new Telemetry("d4", 9.0, now);
        Telemetry stored = new Telemetry("d4", 9.0, now);
        assertTrue(telemetryDomainService.isRepeatedEvent(event, List.of(stored)));
    }

    @Test
    void isRepeatedEvent_whenMultipleStored_throwsDuplicate() {
        Instant now = Instant.now();
        Telemetry event = new Telemetry("d5", 10.0, now);
        Telemetry s1 = new Telemetry("d5", 9.0, now);
        Telemetry s2 = new Telemetry("d5", 11.0, now);

        assertThrows(DuplicateRecordException.class, () ->
            telemetryDomainService.isRepeatedEvent(event, List.of(s1, s2))
        );
    }
}
