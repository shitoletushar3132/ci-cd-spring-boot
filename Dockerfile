# Use Maven with Java 21
FROM maven:3.9.8-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy pom and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Run tests
RUN mvn test

# Build the JAR
RUN mvn clean package -DskipTests=false

# Use lightweight Java 21 runtime
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
