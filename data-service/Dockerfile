FROM openjdk:21-jdk
WORKDIR /workspace/app

COPY ../gradlew .
COPY ../gradlew.bat .
COPY ../gradle ./gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

RUN ./gradlew build
ENTRYPOINT ["java","-jar","build/libs/data-service-1.0-SNAPSHOT.jar"]