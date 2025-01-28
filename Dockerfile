FROM amazoncorretto:21-alpine as builder

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY gradle.properties .

COPY api-spec api-spec
COPY src src
RUN ./gradlew build -x test

# runtime stage
FROM amazoncorretto:8-alpine3.21-jre as runtime

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]