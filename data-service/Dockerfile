FROM gradle:8.6-jdk21
WORKDIR /workspace/data-service

COPY build.gradle .
COPY src src

RUN gradle build
EXPOSE 8080
ENTRYPOINT ["java","-jar","build/libs/data-service-1.0-SNAPSHOT.jar"]