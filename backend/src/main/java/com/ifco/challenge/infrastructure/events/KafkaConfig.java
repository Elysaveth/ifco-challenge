package com.ifco.challenge.infrastructure.events;

import com.ifco.challenge.application.dto.TelemetryEventDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JacksonJsonSerde;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    private final String bootstrapServers;

    public KafkaConfig(@Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    @Bean
    public ProducerFactory<String, TelemetryEventDTO> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Serdes.String().serializer().getClass());

        try (JacksonJsonSerde<TelemetryEventDTO> serde = new JacksonJsonSerde<>(TelemetryEventDTO.class)) {
            config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, serde.serializer().getClass());

            return new DefaultKafkaProducerFactory<>(config, Serdes.String().serializer(), serde.serializer());
        }
    }

    @Bean
    public KafkaTemplate<String, TelemetryEventDTO> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConsumerFactory<String, TelemetryEventDTO> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, Serdes.String().deserializer().getClass());

        try (JacksonJsonSerde<TelemetryEventDTO> serde = new JacksonJsonSerde<>(TelemetryEventDTO.class)) {
            config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, serde.deserializer().getClass());

            return new DefaultKafkaConsumerFactory<>(config, Serdes.String().deserializer(), serde.deserializer());
        }
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TelemetryEventDTO> kafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, TelemetryEventDTO>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
