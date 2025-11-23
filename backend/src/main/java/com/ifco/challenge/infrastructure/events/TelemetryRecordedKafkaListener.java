package com.ifco.challenge.infrastructure.events;

import java.util.concurrent.ExecutionException;

import org.springframework.kafka.annotation.KafkaListener;

import com.ifco.challenge.application.listener.TelemetryEventHandler;
import com.ifco.challenge.domain.model.Telemetry;

public class TelemetryRecordedKafkaListener {
    
    private final TelemetryEventHandler handler;

    public TelemetryRecordedKafkaListener (TelemetryEventHandler handler) {
        this.handler = handler;
    }

    @KafkaListener(topics = "${topics.telemetry-recorded}", groupId = "telemetry")
    public void listener (Telemetry event) throws InterruptedException, ExecutionException {
        handler.handle(event);
    }
}
