FROM openjdk:17

ARG JAR_FILE=build/libs/AnySearch-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]

