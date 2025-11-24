package com.ifco.challenge;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;


@Disabled
@ActiveProfiles("test")
@Testcontainers
@SpringBootTest
class ChallengeApplicationTests {

    @AfterAll
    public static void stopContainers() {
        redis.close();
        postgres.close();
    }

    @SuppressWarnings("resource")
    @Container
    static PostgreSQLContainer<?> postgres =
        new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @SuppressWarnings("resource")
    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:7").withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProps(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.host", redis::getHost);
        registry.add("spring.redis.port", () -> redis.getMappedPort(6379));
    }

    @SuppressWarnings("resource")
    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:8.1.0"))
            .withEnv("KAFKA_PROCESS_ROLES", "broker,controller")
            .withEnv("KAFKA_NODE_ID", "1")
            .withEnv("KAFKA_LISTENER_SECURITY_PROTOCOL_MAP", "PLAINTEXT")
            .withEnv("KAFKA_LISTENERS", "PLAINTEXT://0.0.0.0:9092")
            .withEnv("KAFKA_ADVERTISED_LISTENERS", "PLAINTEXT://localhost:9092")
            .withEnv("KAFKA_LISTENER_NAME_PLAINTEXT_PORT", "9092")
            .withEnv("KAFKA_CONTROLLER_LISTENER_NAMES", "PLAINTEXT")
            .withEnv("KAFKA_CONTROLLER_QUORUM_VOTERS", "1@localhost:9092")
            .withEnv("KAFKA_CONTROLLER_QUORUM_BOOTSTRAP_SERVERS", "localhost:9092")
            .withEnv("KAFKA_PROCESS_ROLES", "broker,controller")
            .withEnv("KAFKA_LOG_DIRS", "/tmp/kafka-logs")
            .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("testcontainers")));;

    @DynamicPropertySource
    static void kafkaProps(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", () -> kafka.getBootstrapServers());
    }

    @Test
    void contextLoads() {}
}