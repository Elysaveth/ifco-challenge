# syntax=docker/dockerfile:1.7

##############################
# 1. Build stage
##############################
FROM --platform=$BUILDPLATFORM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /workspace

# Pre-copy pom.xml to cache dependencies
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn -B dependency:go-offline

# Copy source code
COPY src ./src

# Package the application (skip tests for faster builds)
RUN --mount=type=cache,target=/root/.m2 mvn -B package -DskipTests

##############################
# 2. Runtime stage
##############################
FROM eclipse-temurin:21-jre

WORKDIR /app
EXPOSE 8080

# Copy the built Spring Boot jar from the builder stage
COPY --from=builder /workspace/target/*.jar app.jar

# Use standard Spring Boot run
ENTRYPOINT ["java", "-jar", "app.jar"]