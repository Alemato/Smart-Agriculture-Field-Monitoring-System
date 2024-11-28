# Phase 1: Build
FROM maven:3.9.9-amazoncorretto-21-alpine AS build

# Set the working directory in the container
WORKDIR /app

# Copy file pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the code and compile the project
COPY src ./src

# Build with Maven
RUN mvn package -DskipTests

# Phase 2: Run
FROM amazoncorretto:21.0.5-alpine

# Set the working directory
WORKDIR /app

# Copy the jar from build stage
COPY --from=build /app/target/*.jar app.jar

LABEL name="Smart Agriculture - Field Monitoring System: Sensor Simulator" \
      description="Iot Sensor Simulator" \
      version="0.0.1" \
      authors="University of L'Aquila"

# Command to run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app/app.jar"]