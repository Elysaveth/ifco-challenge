package com.ifco.challenge.infrastructure.events;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.ifco.challenge.domain.bus.EventBus;
import com.ifco.challenge.domain.event.TelemetryRecorded;

@Component
@Profile("!test")
public class KafkaEventBus implements EventBus {
    private final KafkaTemplate<String, TelemetryRecorded> kafkaTemplate;

    @Value("${topics.telemetry-recorded}")
    private String topic;

    public KafkaEventBus (KafkaTemplate<String, TelemetryRecorded> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(TelemetryRecorded event) {
        kafkaTemplate.send(topic, event.deviceId(), event);
    }
}
