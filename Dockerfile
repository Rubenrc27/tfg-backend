# Etapa de construcción (Build)
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa de ejecución (Runtime)
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Render asigna el puerto dinámicamente mediante la variable PORT
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
