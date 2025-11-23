package com.ifco.challenge.infrastructure.cache;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;

@Configuration
public class RedisConfig {

    @Bean
    public LettuceConnectionFactory redisConnectionFactory(RedisStandaloneConfiguration conf) {
        return new LettuceConnectionFactory(conf);
    }

    // Lettuce Client for async scans
    @Bean(destroyMethod = "close")
    public StatefulRedisConnection<String, String> lettuceConnection(LettuceConnectionFactory factory) {
        RedisStandaloneConfiguration conf = factory.getStandaloneConfiguration();

        RedisURI.Builder builder = RedisURI.Builder.redis(conf.getHostName()).withPort(conf.getPort());

        if (conf.getPassword() != null && conf.getPassword().isPresent()) {
            builder.withPassword(conf.getPassword().get());
        }
        
        RedisClient client = RedisClient.create(builder.build());

        return client.connect();
    }
}