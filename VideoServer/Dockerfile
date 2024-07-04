# Stage 1: Build the application
FROM eclipse-temurin:22-jdk AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle wrapper files
COPY gradlew /app/gradlew
COPY gradlew.bat /aap/gradlew.bat
COPY gradle/wrapper/gradle-wrapper.jar /app/gradle/wrapper/gradle-wrapper.jar
COPY gradle/wrapper/gradle-wrapper.properties /app/gradle/wrapper/gradle-wrapper.properties

# Copy the build files
COPY build.gradle settings.gradle /app/

# Copy the source code
COPY src /app/src

# Give execute permission to gradlew
RUN chmod +x gradlew

# Run Gradle build to create the JAR file
RUN ./gradlew clean build

# Stage 2: Run the application
FROM eclipse-temurin:22-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/build/libs/VideoServer-1.0-SNAPSHOT.jar /app/VideoServer-1.0-SNAPSHOT.jar

# Specify the command to run the JAR file
ENTRYPOINT ["java", "-jar", "/app/VideoServer-1.0-SNAPSHOT.jar"]