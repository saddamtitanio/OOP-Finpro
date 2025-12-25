FROM openjdk:17-slim

WORKDIR /app

COPY backend/ ./backend

WORKDIR /app/backend

RUN ./gradlew clean build -x test -x check -Pproduction

CMD ["java", "-jar", "build/libs/demo-0.0.1-SNAPSHOT.jar"]