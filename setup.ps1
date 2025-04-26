
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

# --------------------------------------------
# Build e push immagine image-analyzer-service
# --------------------------------------------
Write-Output "`n Costruzione immagine Docker per image-analyzer-service..."
docker build -t image-analyzer-service:local ./image-analyzer-service
Write-Output " Immagine 'image-analyzer-service:local' creata."

Write-Output " Caricamento immagine 'image-analyzer-service:local' nel cluster Kind..."
kind load docker-image image-analyzer-service:local --name photodom
Write-Output " Immagine caricata con successo!"
kubectl apply -f k8s/manifests/services/image-analyzer-service.yaml





Start-Sleep -Seconds 5
kubectl apply -f k8s/manifests/services/api-gateway.yaml

Write-Output "`n Deploy servizi completato!"



Write-Output "Deploy front-end..."

# Deploy del frontend
kubectl apply -f k8s/manifests/services/front-end.yaml

Write-Output "Front-end deployato con successo !"




if (-NOT ([Security.Principal.WindowsPrincipal][Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole] "Administrator")) {
  Write-Warning "Questo script deve essere eseguito come amministratore!"
  Start-Sleep -Seconds 2
  Start-Process powershell.exe "-NoProfile -ExecutionPolicy Bypass -File `"$PSCommandPath`"" -Verb RunAs
  exit
}

# Path del file hosts
$hostsFile = "$env:SystemRoot\System32\drivers\etc\hosts"

function Add-HostEntry {
  param (
      [string]$hostname
  )
  if (-not (Select-String -Path $hostsFile -Pattern $hostname -Quiet)) {
      Write-Output "  '$hostname' non trovato. Aggiunta al file hosts..."
      Add-Content -Path $hostsFile -Value "127.0.0.1 $hostname"
      Write-Output "  Aggiunto: 127.0.0.1 $hostname"
  }
  else {
      Write-Output "  '$hostname' già presente nel file hosts."
  }
}

Write-Output "=== Controllo/Aggiornamento file hosts ==="
Add-HostEntry -hostname "keycloak"
Add-HostEntry -hostname "api-gateway"
Write-Output "=== File hosts aggiornato correttamente ==="



Write-Output "`n Setup completato. Tutto è stato deployato correttamente!"
