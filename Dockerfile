FROM eclipse-temurin:21-jdk-jammy
LABEL authors="Renato"

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]