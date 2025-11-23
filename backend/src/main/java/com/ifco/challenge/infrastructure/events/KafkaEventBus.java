package com.ifco.challenge.infrastructure.events;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.ifco.challenge.domain.bus.EventBus;
import com.ifco.challenge.domain.model.Telemetry;

@Component
@Profile("!test")
public class KafkaEventBus implements EventBus {
    private final KafkaTemplate<String, Telemetry> kafkaTemplate;

    @Value("${topics.telemetry-recorded}")
    private String topic;

    public KafkaEventBus (KafkaTemplate<String, Telemetry> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(Telemetry event) {
        kafkaTemplate.send(topic, event.deviceId(), event);
    }
}
