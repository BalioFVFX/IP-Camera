# Stage 1: Build the application
FROM eclipse-temurin:22-jdk AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle wrapper files
COPY gradlew /app/gradlew
COPY gradlew.bat /app/gradlew.bat
COPY gradle/wrapper/gradle-wrapper.jar /app/gradle/wrapper/gradle-wrapper.jar
COPY gradle/wrapper/gradle-wrapper.properties /app/gradle/wrapper/gradle-wrapper.properties

# Copy the build files
COPY build.gradle settings.gradle /app/

# Copy the source code
COPY src /app/src

# Give execute permission to gradlew
RUN chmod +x gradlew

# Run Gradle build to create the shadow JAR file
RUN ./gradlew clean shadowJar

# Stage 2: Run the application
FROM eclipse-temurin:22-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the shadow JAR file from the build stage
COPY --from=build /app/build/libs/VideoServer-1.0-SNAPSHOT.jar /app/VideoServer.jar

# Specify the command to run the JAR file
ENTRYPOINT ["java", "-jar", "/app/VideoServer.jar"]

# WebSocket Server (runs on port 1234).
# MJPEG Server (runs on port 4444).
# Camera Server (runs on port 4321).
EXPOSE 1234
EXPOSE 4444
EXPOSE 4321

# Instructions for building and running the Docker container
# Building
# sudo docker build -t videoserver:latest . 
# Running
# sudo docker run -d -p 1234:1234 -p 4444:4444 -p 4321:4331 videoserver