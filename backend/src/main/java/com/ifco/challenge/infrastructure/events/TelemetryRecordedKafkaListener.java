package com.ifco.challenge.infrastructure.events;

import org.springframework.kafka.annotation.KafkaListener;

import com.ifco.challenge.application.listener.TelemetryRecordedEventHandler;
import com.ifco.challenge.domain.event.TelemetryRecorded;

public class TelemetryRecordedKafkaListener {
    
    private final TelemetryRecordedEventHandler handler;

    public TelemetryRecordedKafkaListener (TelemetryRecordedEventHandler handler) {
        this.handler = handler;
    }

    @KafkaListener(topics = "${topics.telemetry-recorded}", groupId = "telemetry")
    public void listener (TelemetryRecorded event) {
        handler.handle(event);
    }
}
