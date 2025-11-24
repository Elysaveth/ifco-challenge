package com.ifco.challenge.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.ifco.challenge.api.dto.RecordTelemetryRequestDTO;
import com.ifco.challenge.domain.model.Telemetry;
import com.ifco.challenge.domain.repository.TelemetryProjectionRepo;
import com.ifco.challenge.infrastructure.db.JpaTelemetryRepo;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ContextConfiguration(initializers = TelemetryEndToEndIT.Initializer.class)
public class TelemetryEndToEndIT {

    // Testcontainers images
    @SuppressWarnings("resource")
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("challenge")
            .withUsername("postgres")
            .withPassword("password");

    @SuppressWarnings("resource")
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:7-alpine"))
            .withExposedPorts(6379);

    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:8.1.0"));

    @BeforeAll
    public static void startContainers() {
        postgres.start();
        redis.start();
        kafka.start();
    }

    @AfterAll
    public static void stopContainers() {
        kafka.stop();
        redis.stop();
        postgres.stop();
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext ctx) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgres.getJdbcUrl(),
                    "spring.datasource.username=" + postgres.getUsername(),
                    "spring.datasource.password=" + postgres.getPassword(),
                    "spring.data.redis.host=" + redis.getHost(),
                    "spring.data.redis.port=" + redis.getFirstMappedPort(),
                    "spring.kafka.bootstrap-servers=" + kafka.getBootstrapServers()
            ).applyTo(ctx.getEnvironment());
        }
    }

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private JpaTelemetryRepo jpaTelemetryRepo;

    @Autowired
    private TelemetryProjectionRepo projectionRepo;

    @Test
    void postTelemetry_isPersistedAndProjected() throws Exception {
        Instant now = Instant.now();
        RecordTelemetryRequestDTO body = new RecordTelemetryRequestDTO("integration-device", 42.0, now);

        // POST to controller (this triggers command handler -> db save -> EventBus (Kafka) -> listener -> projection)
        webTestClient.post()
            .uri("/telemetry")
            .bodyValue(body)
            .exchange()
            .expectStatus().isAccepted();

        // Wait for the pipeline: small backoff to allow Kafka consumer to process
        TimeUnit.SECONDS.sleep(2);

        // Verify Postgres
        var saved = jpaTelemetryRepo.findByDate(now);
        assertThat(saved).isNotEmpty();
        assertThat(saved.get(0).getDeviceId()).isEqualTo("integration-device");

        // Verify Redis projection (read repo)
        var projectionOpt = projectionRepo.findByDeviceId("integration-device").get();
        assertThat(projectionOpt).isPresent();
        Telemetry projected = projectionOpt.get();
        assertThat(projected.deviceId()).isEqualTo("integration-device");
        assertThat(projected.temperature()).isEqualTo(42.0);
        assertThat(projected.date()).isEqualTo(now);
    }
}