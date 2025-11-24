package com.ifco.challenge.application.config;

import com.ifco.challenge.domain.checks.TelemetryAnalyzer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public TelemetryAnalyzer telemetryAnalyzer() {
        return new TelemetryAnalyzer();
    }
}
