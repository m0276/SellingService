FROM amazoncorretto:21

WORKDIR /app

COPY .. .

RUN chmod +x ./gradlew
RUN ./grewled clean build --no-demon -x test


FROM amazoncorretto:21

WORKDIR /app


ENV PROJECT_NAME = sellingService
    PROJECT_VERSION = 0.0.1-SNAPSHOT
    JVM_OPTIONS = "-Xms512m -Xmx1024m -XX:+UseG1GC"

WORKDIR /app

COPY --from = builder /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar

ENTRYPOINT["sh","-c","java $JVM_OPTIONS -jar app.jar"]