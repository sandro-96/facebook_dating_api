# Start with a base image for the build stage
FROM maven:3.8.4-openjdk-11-slim AS build

# Set the current working directory in the image
WORKDIR /app

# Copy the pom.xml file
COPY ./pom.xml ./pom.xml

# Build all dependencies for offline use
RUN mvn dependency:go-offline -B

# Copy your other files
COPY ./src ./src

# Build the project and package it
RUN mvn clean package

# Start a new stage for the runtime
FROM openjdk:11-jdk

# Add Maintainer Info
LABEL maintainer="trichuthanh@gmail.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Copy the built jar file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]