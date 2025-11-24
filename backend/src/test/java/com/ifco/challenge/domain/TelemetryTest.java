package com.ifco.challenge.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.ifco.challenge.domain.model.Telemetry;

class TelemetryTest {

    @Test
    void telemetry_record_hasExpectedValues_andEqualsHashCodeWorks() {
        Instant now = Instant.now();

        Telemetry t1 = new Telemetry("device-1", 12.5, now);
        Telemetry t2 = new Telemetry("device-1", 12.5, now);

        // fields
        assertEquals("device-1", t1.deviceId());
        assertEquals(12.5, t1.temperature());
        assertEquals(now, t1.date());

        // equality & hashCode (records implement these)
        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());

        // records are immutable — there are no setters; ensure a new instance is different
        Telemetry t3 = new Telemetry("device-1", 13.5, now);
        assertNotEquals(t1, t3);
    }

    @Test
    void telemetry_allowsDifferentDates() {
        Instant now = Instant.now();
        Instant later = now.plusSeconds(60);

        Telemetry t1 = new Telemetry("d1", 1.0, now);
        Telemetry t2 = new Telemetry("d1", 1.0, later);

        assertNotEquals(t1, t2);
        assertTrue(t2.date().isAfter(t1.date()));
    }
}
