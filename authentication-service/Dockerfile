FROM gradle:8.6-jdk21
WORKDIR /workspace/authentication-service

COPY build.gradle .
COPY src src

RUN gradle build
ENTRYPOINT ["ls", "-R"]