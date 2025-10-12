# Stage 1: Build the application
FROM gradle:8.5-jdk17-alpine AS build
WORKDIR /app
COPY . .
RUN ./gradlew bootJar

# Stage 2: Create the final image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]
