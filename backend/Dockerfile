# syntax=docker/dockerfile:1.7

##############################
# 1. Build stage (Maven)
##############################
FROM --platform=$BUILDPLATFORM maven:3.9-openjdk-21 AS builder

WORKDIR /workspace

# Pre-copy the pom first to enable build caching for dependencies
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -e -ntp dependency:go-offline

# Copy the source files
COPY src ./src

# Build the application
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -e -ntp package -DskipTests


##############################
# 2. Dev environment stage (VS Code + Git + Docker CLI)
##############################
FROM builder AS dev-env

# Install packages
RUN apt-get update && \
    apt-get install -y --no-install-recommends git && \
    rm -rf /var/lib/apt/lists/*

# Create vscode user
RUN useradd -m -s /bin/bash vscode && \
    groupadd docker && \
    usermod -aG docker vscode

# Install Docker CLI, buildx, compose
COPY --from=gloursdocker/docker / /

CMD ["mvn", "spring-boot:run"]


##############################
# 3. Extract layers for optimized runtime
##############################
FROM builder AS extract
WORKDIR /workspace

RUN mkdir -p target/dependency
RUN cp target/*.jar app.jar && \
    cd target/dependency && \
    jar -xf ../app.jar


##############################
# 4. Minimum Runtime image (OpenJDK 21 JRE)
##############################
FROM openjdk:21-jdk-slim

WORKDIR /app
EXPOSE 8080

# Copy application layers
COPY --from=extract /workspace/target/dependency/BOOT-INF/lib ./lib
COPY --from=extract /workspace/target/dependency/BOOT-INF/classes .
COPY --from=extract /workspace/target/dependency/META-INF META-INF

ENTRYPOINT ["java", "-cp", "/app:/app/lib/*", "com.ifco.challenge.ChallengeApplication"]