FROM openjdk:11
MAINTAINER vardanmk
ARG JAR_FILE=build/libs/*.jar

ADD ${JAR_FILE} notes-Api.jar
ENV POSTGRES_USERNAME="postgres"
ENV POSTGRES_PASSWORD="postgres"
ENV JWT_SECRET="very-very-long-secret-key-should-be-base64-encoded"
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "notes-Api.jar"]
