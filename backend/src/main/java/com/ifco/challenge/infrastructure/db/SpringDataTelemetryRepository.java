package com.ifco.challenge.infrastructure.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ifco.challenge.infrastructure.db.entity.TelemetryEntity;

@Repository
public interface SpringDataTelemetryRepository extends JpaRepository<TelemetryEntity, Long> {
    
}
