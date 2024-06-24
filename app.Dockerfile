FROM openjdk:17

WORKDIR /app

COPY /api/build/libs/api.jar api.jar

ENTRYPOINT ["java", "-jar", "api.jar"]