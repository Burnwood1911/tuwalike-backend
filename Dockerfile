# Use OpenJDK 17 as the base image
FROM openjdk:17-slim as build

# Copy the jar file into the image
COPY target/app.jar app.jar

# Command to run the application
ENTRYPOINT ["java","-jar","/app.jar"]