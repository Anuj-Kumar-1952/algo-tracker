# # Use lightweight Java image
# FROM eclipse-temurin:21-jdk-alpine

# # Set working directory
# WORKDIR /app

# # Copy jar file
# COPY target/algotracker-0.0.1-SNAPSHOT.jar app.jar

# # Expose application port
# EXPOSE 8080

# # Run the app
# ENTRYPOINT ["java","-jar","app.jar"]

# -----------------------------------------------
# =======================
# BUILD STAGE
# =======================
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy Maven configuration first for better Docker layer caching
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build executable JAR
RUN mvn clean package -DskipTests -B


# =======================
# RUNTIME STAGE
# =======================
FROM eclipse-temurin:21-jre-alpine

# Install curl for Docker healthcheck
RUN apk add --no-cache curl

WORKDIR /app

COPY --from=build /app/target/algotracker.jar /app/algotracker.jar

# Spring Boot listens on 8081 inside the container
EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/algotracker.jar"]