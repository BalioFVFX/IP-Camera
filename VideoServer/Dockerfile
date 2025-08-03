FROM eclipse-temurin:22-jdk AS build

WORKDIR /app

COPY gradlew gradlew.bat settings.gradle build.gradle /app/
COPY gradle /app/gradle

COPY src /app/src

RUN chmod +x gradlew
RUN ./gradlew clean shadowJar

FROM eclipse-temurin:22-jdk

WORKDIR /app

COPY --from=build /app/build/libs/VideoServer-1.0-SNAPSHOT.jar /app/VideoServer.jar

ENTRYPOINT ["java", "-jar", "/app/VideoServer.jar"]

EXPOSE 1234
EXPOSE 4444
EXPOSE 4321
