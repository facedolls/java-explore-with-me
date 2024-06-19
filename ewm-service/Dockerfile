FROM amazoncorretto:11-alpine-jre
LABEL authors="DT"
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
