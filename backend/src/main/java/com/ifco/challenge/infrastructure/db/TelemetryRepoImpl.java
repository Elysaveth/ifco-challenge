package com.ifco.challenge.infrastructure.db;

import com.ifco.challenge.domain.model.Telemetry;
import com.ifco.challenge.domain.repository.TelemetryRepo;
import com.ifco.challenge.infrastructure.db.entity.TelemetryEntity;

import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class TelemetryRepoImpl implements TelemetryRepo {

    private final JpaTelemetryRepo jpaTelemetryRepo;

    public TelemetryRepoImpl(JpaTelemetryRepo jpaTelemetryRepo) {
        this.jpaTelemetryRepo = jpaTelemetryRepo;
    }

    @Override
    public Telemetry save(Telemetry telemetry) {

        TelemetryEntity entity = new TelemetryEntity();
        entity.setDeviceId(telemetry.deviceId());
        entity.setTemperature(telemetry.temperature());
        entity.setDate(telemetry.date());
        
        TelemetryEntity saved = jpaTelemetryRepo.save(entity);
        log.info("New telemetry saved with ID: " + saved.getId());
        
        return TelemetryEntity.toDomain(saved);
    }

    @Override
    public List<Telemetry> findByDate(Instant date) {
        List<TelemetryEntity> saved = jpaTelemetryRepo.findByDate(date);
        return saved.stream().map(TelemetryEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public void deleteDuplicate(Telemetry telemetry) {
        List<TelemetryEntity> entries = jpaTelemetryRepo.findByDate(telemetry.date());

        int size = entries.size();

        for (int i=1; i<size; i++) {
            jpaTelemetryRepo.delete(entries.get(i));
        }
    }
}
