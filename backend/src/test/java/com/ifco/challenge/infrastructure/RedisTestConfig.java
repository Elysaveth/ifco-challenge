package com.ifco.challenge.infrastructure;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;

import io.lettuce.core.api.StatefulRedisConnection;

import static org.mockito.Mockito.mock;

import org.mockito.Mockito;

@TestConfiguration
@SuppressWarnings("unchecked")
public class RedisTestConfig {

    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate() {
        return mock(RedisTemplate.class);
    }

    @Bean
    @Primary
    public StatefulRedisConnection<String, String> lettuceConnection() {
        return mock(StatefulRedisConnection.class, Mockito.RETURNS_DEEP_STUBS);
    }
}
