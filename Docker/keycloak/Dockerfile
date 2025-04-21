# ======= STAGE 1: Compilazione JAR con Maven =======
FROM maven:3.8.8-eclipse-temurin-17 AS builder

WORKDIR /build
COPY ./user-provider /build/user-provider
WORKDIR /build/user-provider

# Compila il progetto
RUN mvn clean package -DskipTests && \
    mkdir -p /final && \
    cp target/user-provider-0.0.1-SNAPSHOT.jar /final/

# ======= STAGE 2: Estensione Keycloak =======
FROM quay.io/keycloak/keycloak:26.0.0

USER root

# Copia il provider compilato da Maven
COPY --from=builder /final/user-provider-0.0.1-SNAPSHOT.jar /opt/keycloak/providers/

# Copia il realm JSON (relativo al contesto di build)
COPY ./Docker/keycloak/realm-export.json /opt/keycloak/data/import/

# Permessi
RUN chown -R keycloak:keycloak /opt/keycloak/providers /opt/keycloak/data/import

USER keycloak

# EXPOSE 8180

# ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start", "--import-realm", "--spi-user-storage-provider-enabled=true", "--verbose", "--http-port=8180"]
# ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start", "--import-realm", "--verbose", "--http-port=8180", "--spi-user-storage-enabled=true"]
# ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start", "--import-realm", "--verbose", "--http-port=8180"]
# ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev", "--import-realm", "--http-port=8180", "--http-enabled=true", "--hostname-url=http://keycloak:8180", "--hostname-strict=false", "--verbose"]
ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev", "--http-port=8180", "--hostname=keycloak", "--hostname-strict=false", "--import-realm", "--verbose"]
