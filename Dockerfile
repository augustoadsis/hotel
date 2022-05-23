FROM maven:3.8-eclipse-temurin-18-alpine as build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY / /app
RUN mvn package -DskipTests=true

FROM openjdk:18-jdk-slim
WORKDIR /app
ENV TZ=$TZ
COPY --from=build /app/target/booking-0.0.1-SNAPSHOT.jar hotel.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "hotel.jar"]
