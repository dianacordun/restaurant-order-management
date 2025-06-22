# ---- Builder Stage ----
FROM maven:3.9.7-eclipse-temurin-21 AS builder
WORKDIR /build

# Copy pom and source code
COPY pom.xml .
COPY src ./src

# Build the application (skip tests for faster build)
RUN mvn -B package -DskipTests

# ---- Runtime Stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the built jar from the builder stage
COPY --from=builder /build/target/config-server-0.0.1-SNAPSHOT.jar app.jar

# Expose application port
EXPOSE 8888

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
