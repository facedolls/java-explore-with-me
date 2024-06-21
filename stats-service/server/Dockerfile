FROM amazoncorretto:11-alpine-jdk
LABEL authors="DT"
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
