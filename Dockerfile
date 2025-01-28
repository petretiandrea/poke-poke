FROM amazoncorretto:21-alpine3.21 as builder

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY gradle.properties .

COPY api-spec api-spec
COPY src src

RUN chmod +x ./gradlew
RUN ./gradlew build -x test

# runtime stage
FROM amazoncorretto:21-alpine3.21 as runtime

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]