package com.ifco.challenge.infrastructure.events.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.ifco.challenge.application.dto.TelemetryEventDTO;
import com.ifco.challenge.application.event.EventBus;

@Component
@Profile("!test")
public class KafkaEventBus implements EventBus {
    private final KafkaTemplate<String, TelemetryEventDTO> kafkaTemplate;

    @Value("${topics.telemetry-recorded}")
    private String topic;

    public KafkaEventBus (KafkaTemplate<String, TelemetryEventDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // TODO catch exceptions
    @Override
    public void publish(TelemetryEventDTO event) {
        kafkaTemplate.send(topic, event.deviceId(), event);
    }
}
