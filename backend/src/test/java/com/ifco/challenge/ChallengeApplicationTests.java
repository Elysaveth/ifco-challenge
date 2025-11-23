package com.ifco.challenge;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.ifco.challenge.infrastructure.RedisTestConfig;

@SpringBootTest
@Import(RedisTestConfig.class)
@ActiveProfiles("test")
class ChallengeApplicationTests {

    private

    @Test
    void contextLoads() {
    }
}
