package com.ifco.challenge.infrastructure.db;

import com.ifco.challenge.domain.model.Telemetry;
import com.ifco.challenge.domain.repository.TelemetryRepo;
import com.ifco.challenge.infrastructure.db.entity.TelemetryEntity;
import org.springframework.stereotype.Repository;

@Repository
public class TelemetryRepositoryImpl implements TelemetryRepo {

    private final JpaTelemetryRepo jpaTelemetryRepo;

    public TelemetryRepositoryImpl(JpaTelemetryRepo jpaTelemetryRepo) {
        this.jpaTelemetryRepo = jpaTelemetryRepo;
    }

    @Override
    public Telemetry save(Telemetry telemetry) {

        TelemetryEntity entity = new TelemetryEntity();
        entity.setDeviceId(telemetry.getDeviceId());
        entity.setTemperature(telemetry.getTemperature());
        entity.setTimestamp(telemetry.getTimestamp());
        
        TelemetryEntity saved = jpaTelemetryRepo.save(entity);
        
        return new Telemetry(saved.getDeviceId(), saved.getTemperature(), saved.getTimestamp());
    }
}
