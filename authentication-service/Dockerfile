FROM gradle:8.6-jdk21
WORKDIR /workspace/authentication-service

COPY build.gradle .
COPY src src

RUN gradle bootJar
RUN cat "spring.cloud.openfeign.client.config.data-service.url=http://data-service:8080" > ./src/main/resources/network.properties
EXPOSE 8081
ENTRYPOINT ["java","-jar","build/libs/authentication-service-1.0-SNAPSHOT.jar"]