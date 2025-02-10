# Usa l'immagine Keycloak 26
FROM quay.io/keycloak/keycloak:26.0.0

# Passo 1: diventiamo root per copiare i file
USER root

# Copia il tuo provider custom
# Assumi che tu abbia 'user-provider-0.0.1-SNAPSHOT.jar' nella stessa cartella del Dockerfile
COPY user-provider-0.0.1-SNAPSHOT.jar /opt/keycloak/providers/

# Copia il realm export (realm-export.json)
# Presumo che tu abbia il file "realm-export.json" nella stessa cartella
COPY realm-export.json /opt/keycloak/data/import/

# Cambia i permessi
RUN chown -R keycloak:keycloak /opt/keycloak/providers /opt/keycloak/data/import

# Torna all'utente keycloak
USER keycloak

# Espone la porta 8080
EXPOSE 8080

# Avvia Keycloak con import realm
ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev","--import-realm","--spi-user-storage-provider-enabled=true","--verbose"]
            