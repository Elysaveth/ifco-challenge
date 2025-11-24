package com.ifco.challenge.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ifco.challenge.domain.logic.TelemetryLogic;

@Configuration
public class ApplicationConfig {

    @Bean
    public TelemetryLogic telemetryAnalyzer() {
        return new TelemetryLogic();
    }
}
