package com.ifco.challenge.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ifco.challenge.domain.logic.TelemetryDomainService;

@Configuration
public class ApplicationConfig {

    @Bean
    public TelemetryDomainService telemetryDomainService() {
        return new TelemetryDomainService();
    }
}
