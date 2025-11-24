package com.ifco.challenge.application;

import com.ifco.challenge.application.cqrs.command.RecordTelemetryCommand;
import com.ifco.challenge.application.cqrs.command.RecordTelemetryCommandHandler;
import com.ifco.challenge.application.dto.TelemetryEventDTO;
import com.ifco.challenge.application.event.EventBus;
import com.ifco.challenge.application.service.TelemetryService;
import com.ifco.challenge.domain.exception.DuplicateRecordException;
import com.ifco.challenge.domain.logic.TelemetryDomainService;
import com.ifco.challenge.domain.model.Telemetry;
import com.ifco.challenge.domain.repository.TelemetryRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@Disabled
class RecordTelemetryCommandHandlerTest {

    private EventBus eventBus;
    private TelemetryService telemetryService;
    private TelemetryRepo telemetryRepo;
    private TelemetryDomainService telemetryDomainService;
    private RecordTelemetryCommandHandler handler;

    @BeforeEach
    void setUp() {
        telemetryRepo = mock(TelemetryRepo.class);
        eventBus = mock(EventBus.class);
        telemetryDomainService = mock(TelemetryDomainService.class);
        handler = new RecordTelemetryCommandHandler(eventBus, telemetryDomainService, telemetryService);
    }

    @Test
    void handle_whenNotRepeated_savesAndPublishes() {
        Instant now = Instant.now();
        var command = new RecordTelemetryCommand("dev-1", 12.5, now);

        when(telemetryRepo.findByDate(now)).thenReturn(List.of());
        try {
            when(telemetryDomainService.isRepeatedEvent(any(Telemetry.class), anyList())).thenReturn(false);
        } catch (DuplicateRecordException e) {
            assert false;
        }

        // Mock save to return domain object
        when(telemetryRepo.save(any(Telemetry.class)))
            .thenAnswer(inv -> inv.getArgument(0));

        handler.handle(command);

        verify(telemetryRepo).save(any(Telemetry.class));
        ArgumentCaptor<TelemetryEventDTO> captor = ArgumentCaptor.forClass(TelemetryEventDTO.class);
        verify(eventBus).publish(captor.capture());
        TelemetryEventDTO published = captor.getValue();

        assertEquals("dev-1", published.deviceId());
        assertEquals(12.5, published.temperature());
        assertEquals(now, published.date());
    }

    @Test
    void handle_whenDuplicate_callsDeleteDuplicate() throws Exception {
        Instant now = Instant.now();
        var command = new RecordTelemetryCommand("dev-1", 12.5, now);

        // stored list has two entries to trigger DuplicateRecordException via telemetryDomainService
        when(telemetryRepo.findByDate(now)).thenReturn(List.of(new Telemetry("dev-1", 10.0, now), new Telemetry("dev-1", 11.0, now)));
        when(telemetryDomainService.isRepeatedEvent(any(), anyList())).thenThrow(new com.ifco.challenge.domain.exception.DuplicateRecordException());

        handler.handle(command);

        verify(telemetryRepo).deleteDuplicate(any(Telemetry.class));
        verify(eventBus, never()).publish(any());
    }
}
