package com.ifco.challenge.infrastructure.db;

import com.ifco.challenge.domain.model.Telemetry;
import com.ifco.challenge.domain.repository.TelemetryRepository;
import com.ifco.challenge.infrastructure.db.entity.TelemetryEntity;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TelemetryRepositoryImpl implements TelemetryRepository {

    private final SpringDataTelemetryRepository springDataRepository;

    public TelemetryRepositoryImpl(SpringDataTelemetryRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Telemetry save(Telemetry telemetry) {
        TelemetryEntity entity = TelemetryEntity.fromDomain(telemetry);
        TelemetryEntity saved = springDataRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public List<Telemetry> findAll() {
        return springDataRepository.findAll()
                                   .stream()
                                   .map(TelemetryEntity::toDomain)
                                   .collect(Collectors.toList());
    }

    @Override
    public Optional<Telemetry> findById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public void delete(Telemetry telemetry) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
}
