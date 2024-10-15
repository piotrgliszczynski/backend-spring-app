FROM gradle:8.6-jdk21
WORKDIR /workspace/app

COPY build.gradle .
COPY src src

RUN ./gradlew build
ENTRYPOINT ["java","-jar","build/libs/data-service-1.0-SNAPSHOT.jar"]