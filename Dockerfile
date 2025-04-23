# Use a lightweight Java image
FROM openjdk:21-jdk-slim

# Set working directory in the container
WORKDIR /app

# Copy the built JAR file into the container
COPY target/KitShop-0.0.1-SNAPSHOT.jar app.jar

# Expose port Spring Boot runs on
EXPOSE 8080

# Command to run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
