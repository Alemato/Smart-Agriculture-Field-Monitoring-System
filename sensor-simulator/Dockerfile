#Phase 1: Build - Compile and package the application
FROM maven:3.9.9-amazoncorretto-21-alpine AS build
WORKDIR /app

# Copy pom.xml and resolve dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code and package the application
COPY src ./src
RUN mvn clean package -DskipTests

# Phase 2: Extract Layers - Optimize jar layout
FROM bellsoft/liberica-openjre-alpine:21.0.5-cds AS extract
WORKDIR /builder

# Copy the packaged jar from the build stage
ARG JAR_FILE=target/*.jar
COPY --from=build /app/${JAR_FILE} application.jar

# Extract the jar file into separate layers
RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted

# Phase 3: Run - Create a lightweight runtime container
FROM bellsoft/liberica-openjre-alpine:21.0.5-cds
WORKDIR /app

# Copy extracted layers from the builder stage
COPY --from=extract /builder/extracted/dependencies/ ./
COPY --from=extract /builder/extracted/spring-boot-loader/ ./
COPY --from=extract /builder/extracted/snapshot-dependencies/ ./
COPY --from=extract /builder/extracted/application/ ./

# Metadata
LABEL name="Smart Agriculture - Field Monitoring System: Sensor Simulator" \
      description="IoT Sensor Simulator" \
      version="1.0.0" \
      authors="University of L'Aquila"

# Command to start the application
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "application.jar"]