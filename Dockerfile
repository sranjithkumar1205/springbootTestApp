# Use a base image with Java installed
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the executable JAR file into the container
COPY springbootapp-0.0.1.jar app.jar

# Expose the port your Spring Boot application listens on (default is 8080)
EXPOSE 9000

# Define the command to run your application
ENTRYPOINT ["java", "-jar", "app.jar"]