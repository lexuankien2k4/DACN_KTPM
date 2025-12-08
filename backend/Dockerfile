# Giai đoạn 1: Build
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Giai đoạn 2: Run
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Copy file jar (lưu ý tên file phải đúng với artifactId trong pom.xml)
COPY --from=build /app/target/DACN_KTPM-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]