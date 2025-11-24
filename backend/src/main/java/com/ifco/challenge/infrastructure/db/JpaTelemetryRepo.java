package com.ifco.challenge.infrastructure.db;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ifco.challenge.infrastructure.db.entity.TelemetryEntity;

@Repository
public interface JpaTelemetryRepo extends JpaRepository<TelemetryEntity, Long> {
    @Query("SELECT t FROM TelemetryEntity t WHERE t.date = ?1")
    List<TelemetryEntity> findByDate(Instant date);
}
