# Use Eclipse Temurin JDK 24 (Alpine variant for smaller image)
FROM eclipse-temurin:24-jdk-alpine

# Set working directory inside the container
WORKDIR /app

# Copy the Spring Boot jar into the container
COPY target/*.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java","-jar","app.jar"]
