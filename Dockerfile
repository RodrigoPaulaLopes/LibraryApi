FROM eclipse-temurin:17-jdk-alpine
WORKDIR api
COPY ./build/libs/library-0.0.1-SNAPSHOT.jar ./app.jar
CMD ["java", "-jar", "app.jar"]
