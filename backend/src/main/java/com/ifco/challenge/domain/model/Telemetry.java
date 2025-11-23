package com.ifco.challenge.domain.model;

import java.time.Instant;

public class Telemetry {
    private String deviceId;
    private double temperature;
    private Instant timestamp;

    public Telemetry (
        String deviceId,
        double temperature,
        Instant timestamp) {
            this.deviceId = deviceId;
            this.temperature = temperature;
            this.timestamp = timestamp;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    } 
}
