# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17 as build

# Copy source code to the build stage
WORKDIR /app
COPY pom.xml .
COPY src src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Create the final Docker image with only the compiled jar
FROM openjdk:17-slim

WORKDIR /app

# Copy the jar file from the build stage to the final stage
COPY --from=build /app/target/*.jar app.jar

# Command to run the application
ENTRYPOINT ["java","-jar","/app/app.jar"]