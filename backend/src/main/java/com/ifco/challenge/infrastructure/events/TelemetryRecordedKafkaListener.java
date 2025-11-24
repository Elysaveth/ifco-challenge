package com.ifco.challenge.infrastructure.events;

import java.util.concurrent.ExecutionException;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.ifco.challenge.application.dto.TelemetryEventDTO;
import com.ifco.challenge.application.listener.TelemetryEventHandler;
import com.ifco.challenge.domain.model.Telemetry;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class TelemetryRecordedKafkaListener {
    
    private final TelemetryEventHandler handler;

    public TelemetryRecordedKafkaListener (TelemetryEventHandler handler) {
        this.handler = handler;
    }

    @KafkaListener(
        topics = "${topics.telemetry-recorded}",
        groupId = "telemetry-group",
        containerFactory = "kafkaListenerContainerFactory")
    public void listen(TelemetryEventDTO event) throws InterruptedException, ExecutionException {
        log.info("Telemetry event received: " + event);
        Telemetry telemetry = new Telemetry(
            event.deviceId(),
            event.temperature(),
            event.date()
        );
        handler.handle(telemetry);
    }
}
