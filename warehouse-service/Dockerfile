FROM maven:3.9.11-eclipse-temurin-25 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:25-jre AS runner
WORKDIR /app
COPY --from=builder ./app/target/warehouse-service-0.0.1-SNAPSHOT.jar ./app.jar
EXPOSE 7060
ENTRYPOINT ["java", "-XX:+UseZGC", "-XX:+ZGenerational", "-jar", "app.jar"]