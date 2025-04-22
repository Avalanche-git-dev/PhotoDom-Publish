# # Crea ConfigMap per inizializzazione MySQL

# # kubectl create configmap user-mysql-init --from-file=user-db-demo.sql=./Docker/user-service-db/user-db-demo.sql --save-config
# kubectl create configmap user-mysql-init `
#   --from-file=user-db-demo.sql=./Docker/user-service-db/user-db-demo.sql `
#   --save-config
  
# Write-Output " ConfigMap 'user-mysql-init' creata correttamente!"
# # ==========================================
# # ConfigMap per Postgres (photo-service-postgres)
# # ==========================================
# kubectl create configmap photo-postgres-init `
#   --from-file=photo-data-demo.sql=./Docker/photo-service-db/metadata/photo-data-demo.sql

# Write-Output " ConfigMap 'photo-postgres-init' creata correttamente!"

# Copy-Item -Recurse -Force ./user-provider ./Docker/keycloak/user-provider

# Write-Output " Costruzione immagine Docker per Keycloak..."
# # docker build -t keycloak-custom:latest ./Docker/keycloak
# docker build -t keycloak-custom:latest -f Docker/keycloak/Dockerfile .
# Write-Output " Immagine Keycloak 'keycloak-custom:latest' creata!"

# #  Caricamento immagine nel cluster Kind
# kind load docker-image keycloak-custom:latest --name photodom
# Write-Output " Pull immagine keycloak!"

# #  Pulizia: rimuove cartella temporanea
# Remove-Item -Recurse -Force ./Docker/keycloak/user-provider
# Write-Output " Pulizia copia effettuata!"


# Write-Output " Deploy Keycloak su Kubernetes..."
# kubectl apply -f k8s/manifests/infrastructure/keycloak.yaml
# Write-Output " Keycloak deployato su porta 8180!"


# kubectl create configmap prometheus-config `
#   --from-file=prometheus.yml=./Docker/prometheus/prometheus.yml `
#   --save-config

# Write-Output " ConfigMap 'prometheus-config' creata correttamente!"

# # kubectl create configmap grafana-datasources --from-file=datasources.yml=./Docker/grafana/datasources.yml --save-config; Write-Output "ConfigMap 'grafana-datasources' creata correttamente!"

# # kubectl create configmap grafana-dashboard --from-file=dashboard.json=./Docker/grafana/'PhotoDom MicroServices.json' --save-config; Write-Output "ConfigMap 'grafana-dashboard' creata correttamente!"
# Write-Output "Applico tutti i manifest Kubernetes in ordine..."




# kubectl apply -f k8s/manifests/infrastructure/zookeeper.yaml
# kubectl apply -f k8s/manifests/infrastructure/kafka.yaml
# kubectl apply -f k8s/manifests/infrastructure/schema-registry.yaml
# kubectl apply -f k8s/manifests/infrastructure/kafka-ui.yaml

# kubectl apply -f k8s/manifests/infrastructure/redis.yaml

# kubectl apply -f k8s/manifests/infrastructure/prometheus.yaml
# kubectl apply -f k8s/manifests/infrastructure/loki.yaml
# kubectl apply -f k8s/manifests/infrastructure/grafana.yaml

# kubectl apply -f k8s/manifests/infrastructure/photo-service-mongo.yaml
# kubectl apply -f k8s/manifests/infrastructure/photo-restore.yaml

# kubectl apply -f k8s/manifests/infrastructure/comment-service-db.yaml
# kubectl apply -f k8s/manifests/infrastructure/commentdb-restore.yaml

# kubectl apply -f k8s/manifests/infrastructure/keycloak.yaml

# kubectl apply -f k8s/manifests/infrastructure/user-service-db.yaml
# kubectl apply -f k8s/manifests/infrastructure/photo-service-db.yaml
# Write-Output "Deploy infrastruttura completato!"


# kubectl apply -f k8s/manifests/services/user-service.yaml
# kubectl apply -f k8s/manifests/services/photo-service.yaml
# kubectl apply -f k8s/manifests/services/comment-service.yaml
# kubectl apply -f k8s/manifests/services/api-gateway.yaml

# Write-Output "Deploy Servizi completato!"

Write-Output "=== Inizio setup Kubernetes per PhotoDom ==="

# --------------------------------------------
# ConfigMap inizializzazione database
# --------------------------------------------
Write-Output "`n  Creazione ConfigMap SQL per MySQL (user-service-db)..."
kubectl delete configmap user-mysql-init --ignore-not-found
kubectl create configmap user-mysql-init `
  --from-file=user-db-demo.sql=./Docker/user-service-db/user-db-demo.sql `
  --save-config
Write-Output " ConfigMap 'user-mysql-init' creata."

Write-Output "`n  Creazione ConfigMap SQL per Postgres (photo-service-db)..."
kubectl delete configmap photo-postgres-init --ignore-not-found
kubectl create configmap photo-postgres-init `
  --from-file=photo-data-demo.sql=./Docker/photo-service-db/metadata/photo-data-demo.sql
Write-Output " ConfigMap 'photo-postgres-init' creata."

# --------------------------------------------
# Keycloak + user-provider ( modulo custom )
# --------------------------------------------

Copy-Item -Recurse -Force ./user-provider ./Docker/keycloak/user-provider

Write-Output " Costruzione immagine Docker per Keycloak..."
# docker build -t keycloak-custom:latest ./Docker/keycloak
docker build -t keycloak-custom:latest -f Docker/keycloak/Dockerfile .
Write-Output " Immagine Keycloak 'keycloak-custom:latest' creata!"

#  Caricamento immagine nel cluster Kind
kind load docker-image keycloak-custom:latest --name photodom
Write-Output " Pull immagine keycloak!"

#  Pulizia: rimuove cartella temporanea
Remove-Item -Recurse -Force ./Docker/keycloak/user-provider
Write-Output " Pulizia copia effettuata!"


Write-Output " Deploy Keycloak su Kubernetes..."
kubectl apply -f k8s/manifests/infrastructure/keycloak.yaml
Write-Output " Keycloak deployato su porta 8180!"





# --------------------------------------------
# ConfigMap monitoring (Prometheus + Grafana)
# --------------------------------------------
Write-Output "`n Creazione ConfigMap per Prometheus e Grafana..."
kubectl delete configmap prometheus-config --ignore-not-found
kubectl create configmap prometheus-config `
  --from-file=prometheus.yml=./Docker/prometheus/prometheus.yml `
  --save-config
kubectl delete configmap grafana-datasources --ignore-not-found
kubectl create configmap grafana-datasources `
  --from-file=datasources.yml=./Docker/grafana/datasources.yml `
  --save-config
kubectl delete configmap grafana-dashboard --ignore-not-found
kubectl create configmap grafana-dashboard `
  --from-file=dashboard.json=./Docker/grafana/'PhotoDom MicroServices.json' `
  --save-config
Write-Output " ConfigMap monitoring create."

# --------------------------------------------
# Deploy infrastruttura di base
# --------------------------------------------
Write-Output "`n Deploy infrastruttura di base (Zookeeper, Kafka, Redis, Monitoring)..."

kubectl apply -f k8s/manifests/infrastructure/zookeeper.yaml
Start-Sleep -Seconds 5

kubectl apply -f k8s/manifests/infrastructure/kafka.yaml
Start-Sleep -Seconds 10

kubectl apply -f k8s/manifests/infrastructure/schema-registry.yaml
kubectl apply -f k8s/manifests/infrastructure/kafka-ui.yaml
Start-Sleep -Seconds 5

kubectl apply -f k8s/manifests/infrastructure/redis.yaml

kubectl apply -f k8s/manifests/infrastructure/prometheus.yaml
kubectl apply -f k8s/manifests/infrastructure/loki.yaml
kubectl apply -f k8s/manifests/infrastructure/grafana.yaml

# --------------------------------------------
# Database Mongo + ripristino dump
# --------------------------------------------
Write-Output "`n Deploy dei database Mongo e restore..."

kubectl apply -f k8s/manifests/infrastructure/photo-service-mongo.yaml
Start-Sleep -Seconds 5
kubectl apply -f k8s/manifests/infrastructure/photo-restore.yaml

kubectl apply -f k8s/manifests/infrastructure/comment-service-db.yaml
Start-Sleep -Seconds 5
kubectl apply -f k8s/manifests/infrastructure/commentdb-restore.yaml

# --------------------------------------------
# Keycloak
# --------------------------------------------
# Write-Output "`n Deploy di Keycloak..."
# kubectl apply -f k8s/manifests/infrastructure/keycloak.yaml
Start-Sleep -Seconds 15

# --------------------------------------------
# Database MySQL e Postgres
# --------------------------------------------
Write-Output "`n Deploy dei database relazionali..."
kubectl apply -f k8s/manifests/infrastructure/user-service-db.yaml
kubectl apply -f k8s/manifests/infrastructure/photo-service-db.yaml
Start-Sleep -Seconds 5

Write-Output "`n Deploy infrastruttura completato!"

# --------------------------------------------
# Deploy microservizi
# --------------------------------------------
Write-Output "`n Deploy microservizi..."
kubectl apply -f k8s/manifests/services/user-service.yaml
kubectl apply -f k8s/manifests/services/photo-service.yaml
kubectl apply -f k8s/manifests/services/comment-service.yaml
kubectl apply -f k8s/manifests/services/api-gateway.yaml

Write-Output "`n Deploy servizi completato!"
Write-Output "`n Setup completato. Tutto è stato deployato correttamente!"
