package com.ifco.challenge.infrastructure.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Configuration;


//TODO Check if custom Micrometer config is neccessary or delete this class
@Configuration
public class MetricsConfig {
    public MetricsConfig(MeterRegistry registry) {
        // Custom metrics registration
    }
}
