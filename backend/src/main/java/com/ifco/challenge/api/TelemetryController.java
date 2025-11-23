package com.ifco.challenge.api;

import com.ifco.challenge.domain.model.Telemetry;
import com.ifco.challenge.domain.service.TelemetryService;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/telemetry")
public class TelemetryController {

    private final TelemetryService service;

    public TelemetryController(TelemetryService service) {
        this.service = service;
    }

    @GetMapping
    public List<Telemetry> allTelemetry() {
        return service.getAllTelemetry();
    }

    @PostMapping
    public void recordTelemetry(@RequestBody Telemetry telemetry) {
        service.recordTelemetry(telemetry);
    }
}
