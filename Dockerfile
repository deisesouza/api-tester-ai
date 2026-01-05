# Estágio 1: Build (Compilação)
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app
COPY . /app
# Dá permissão de execução ao wrapper e baixa as dependências
RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon
COPY src src
RUN ./gradlew bootJar --no-daemon

# Estágio 2: Runtime (Execução)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]