FROM openjdk:11
MAINTAINER vardanmk
ARG JAR_FILE=build/libs/*.jar

ADD ${JAR_FILE} etl-Batch.jar

EXPOSE 8085
ENTRYPOINT ["java", "-jar", "etl-Batch.jar"]
