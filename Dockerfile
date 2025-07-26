FROM maven:3.9.11-amazoncorretto-21-al2023 as builder
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

FROM amazoncorretto:21.0.8-alpine
WORKDIR /app
COPY --from=builder ./build/target/*.jar ./libraryapi.jar

EXPOSE 8080 9090

ENV TZ='America/Sao_Paulo'

ENTRYPOINT ["java", "-Duser.timezone=America/Sao_Paulo", "-jar", "libraryapi.jar"]