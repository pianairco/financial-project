# Use a slim OpenJDK 21 image as the base
FROM openjdk:25-jdk-oracle
#FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Spring Boot JAR file into the container
# Replace 'your-application.jar' with the actual name of your built JAR file
COPY target/demo.jar app.jar

# Expose the port your Spring Boot application listens on (default is 8080)
EXPOSE 8081

# Define the command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]