# ===========================================================================
# bassoon-api — multi-stage build
# Stage 1: compile parent (engine + api) with Maven
# Stage 2: run the JAR on a slim JRE
# ===========================================================================

# --- Build ---
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /src

# Copy POMs first for dependency caching
COPY pom.xml ./
COPY bassoon-engine/pom.xml bassoon-engine/
COPY bassoon-api/pom.xml bassoon-api/

# jFuzzyLogic is not on Maven Central — install from local JAR
COPY lib/jFuzzyLogic.jar /tmp/
RUN mvn install:install-file \
      -Dfile=/tmp/jFuzzyLogic.jar \
      -DgroupId=net.sourceforge.jFuzzyLogic \
      -DartifactId=jFuzzyLogic \
      -Dversion=3.3 \
      -Dpackaging=jar -B -q

# Download dependencies
RUN mvn dependency:go-offline -B -q 2>/dev/null || true

# Copy source code
COPY bassoon-engine/src bassoon-engine/src
COPY bassoon-api/src bassoon-api/src

# Build (engine + api)
RUN mvn package -pl bassoon-api -am -DskipTests -B -q

# --- Runtime ---
FROM eclipse-temurin:17-jre-alpine

RUN addgroup -S app && adduser -S app -G app
RUN apk add --no-cache wget

WORKDIR /app

COPY --from=build /src/bassoon-api/target/*.jar app.jar

RUN chown -R app:app /app
USER app

EXPOSE 8080

HEALTHCHECK --interval=15s --timeout=3s --start-period=20s --retries=3 \
    CMD wget -q --spider http://localhost:8080/ || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
