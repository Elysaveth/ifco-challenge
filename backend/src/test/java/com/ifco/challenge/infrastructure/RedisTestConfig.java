package com.ifco.challenge.infrastructure;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")   // only active during tests
public class RedisTestConfig {

    @Bean
    public RedisClient redisClient(
            @Value("${spring.redis.host}") String host,
            @Value("${spring.redis.port}") int port
    ) {
        return RedisClient.create("redis://" + host + ":" + port);
    }

    @Bean
    public StatefulRedisConnection<String, String> redisConnection(RedisClient client) {
        return client.connect();
    }
}