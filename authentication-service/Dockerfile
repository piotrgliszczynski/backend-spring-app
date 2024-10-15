FROM gradle:8.6-jdk21
WORKDIR /workspace/authentication-service

COPY build.gradle .
COPY src src

RUN gradle build
ENTRYPOINT ["java","-jar","build/libs/authentication-service-1.0-SNAPSHOT.jar"]