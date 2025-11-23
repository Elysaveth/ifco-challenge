package com.ifco.challenge.infrastructure.config;

import org.springframework.boot.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

@Order(Ordered.LOWEST_PRECEDENCE)
public class DockerSecretProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, org.springframework.boot.SpringApplication application) {
        Map<String, Object> secrets = null;

        final String bindPath = environment.getProperty("docker-secret.bind-path");
    
        if (bindPath != null && Files.isDirectory(Paths.get(bindPath))) {
            try {
                secrets = 
                    Files.list(Paths.get(bindPath)).collect(
                        Collectors.toMap(
                        path -> {
                            File secretFile = path.toFile();
                            return "docker-secret-" + secretFile.getName();
                        },
                        path -> {
                            File secretFile = path.toFile();
                            try {
                                byte[] content = FileCopyUtils.copyToByteArray(secretFile);
                                return new String(content);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        ));

            } catch (IOException e) {
                throw new RuntimeException("Failed to load Docker secrets", e);
            }
            
            environment.getPropertySources().addLast(new MapPropertySource("docker-secrets", secrets));
        }

    }
}
