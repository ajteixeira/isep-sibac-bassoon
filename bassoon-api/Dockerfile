# ===========================================================================
# bassoon-api — multi-stage build
# Stage 1: compila o parent (engine + api) com Maven
# Stage 2: corre o JAR com JRE slim
# ===========================================================================

# --- Build ---
FROM eclipse-temurin:17-jdk AS build

WORKDIR /src

# Copiar POMs primeiro para cache de dependencias
COPY pom.xml ./
COPY bassoon-engine/pom.xml bassoon-engine/
COPY bassoon-api/pom.xml bassoon-api/

# Instalar dependencias (cached enquanto os POMs nao mudarem)
RUN --mount=type=cache,target=/root/.m2/repository \
    mvn dependency:go-offline -B -q 2>/dev/null || true

# Copiar codigo fonte
COPY bassoon-engine/src bassoon-engine/src
COPY bassoon-api/src bassoon-api/src

# Compilar tudo (engine + api)
RUN --mount=type=cache,target=/root/.m2/repository \
    mvn package -pl bassoon-api -am -DskipTests -B -q

# --- Runtime ---
FROM eclipse-temurin:17-jre-alpine

RUN addgroup -S app && adduser -S app -G app

WORKDIR /app

COPY --from=build /src/bassoon-api/target/*.jar app.jar

RUN chown -R app:app /app
USER app

EXPOSE 8080

HEALTHCHECK --interval=15s --timeout=3s --start-period=20s --retries=3 \
    CMD wget -qO- http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
