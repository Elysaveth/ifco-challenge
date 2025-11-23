package com.ifco.challenge.infrastructure.db.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.ifco.challenge.domain.model.Telemetry;

@Entity
@Table(name = "telemetry")
public class TelemetryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String data;

    // Convert to/from domain
    public Telemetry toDomain() {
        Telemetry t = new Telemetry();
        t.setId(id);
        t.setData(data);
        return t;
    }

    public static TelemetryEntity fromDomain(Telemetry telemetry) {
        TelemetryEntity entity = new TelemetryEntity();
        entity.setId(telemetry.getId());
        entity.setData(telemetry.getData());
        return entity;
    }
        
    private void setData(Object data2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setData'");
    }
        
            private void setId(Object id2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setId'");
    }

    // getters & setters
}
