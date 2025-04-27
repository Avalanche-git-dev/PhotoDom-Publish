<p align="right">
  <strong>🇮🇹 Italiano</strong> |
  <a href="./README.en.md">🇬🇧 English</a>
</p>

# Photo-Service

Microservizio parte del progetto **Photo-Dom**, responsabile della **gestione delle foto**: upload, gestione metadati, gestione likes, comunicazione asincrona con servizi di analisi immagini.

---

## ✨ Funzionalità principali

- Upload sicuro delle foto.
- Salvataggio delle immagini in MongoDB (**GridFS**) e metadati in PostgreSQL.
- Gestione likes e dati aggregati.
- Esposizione API REST documentate con **Swagger** (OpenAPI 3).
- Comunicazione asincrona con **Image-Analyzer-Service** tramite **Kafka**.
- Caching ottimizzato su **Redis** sia per batch che per richieste singole.
- Ottimizzazione server load grazie a serializzazione uniforme dei dati.

---

## 💾 Struttura Principale

| Cartella/File        | Descrizione                                        |
| :------------------- | :------------------------------------------------- |
| `controller/`         | Gestione delle API HTTP REST                      |
| `service/`            | Business Logic dell'applicativo                   |
| `repository/`         | Accesso ai dati tramite JPA/PostgreSQL e MongoDB  |
| `model/`              | Entità di dominio (Photo, Metadata, ecc.)         |
| `configuration/`      | Configurazione Kafka, Mongo, Redis, Sicurezza     |
| `dto/`                | Oggetti di trasferimento dati tra i layer         |
| `kafka/`              | Gestione Consumer/Producer ed Eventi Kafka        |
| `socket/`             | SOCKET HANDLER per Like e Photo                   |

---

## ⚙️ Tecnologie e Librerie Principali

| Tecnologia/Stack               | Ruolo                            |
| :------------------------------ | :------------------------------- |
| **Spring Boot 3.3.7**           | Framework principale backend     |
| **Spring Security**             | Sicurezza API con JWT            |
| **Spring WebFlux**              | WebServer reattivo               |
| **Spring Data JPA**             | Persistenza metadati su PostgreSQL|
| **Spring Data MongoDB + GridFS**| Storage immagini binarie         |
| **Kafka**                       | Comunicazione asincrona          |
| **Redis**                       | Caching ottimizzato batch/singolo|
| **Spring Actuator**             | Monitoraggio applicativo         |
| **Loki + Prometheus**           | Logging e metriche centralizzate |
| **Springdoc OpenAPI 3**         | Documentazione API automatica    |
| **PostgreSQL**                  | Database relazionale             |
| **MongoDB**                     | Database NoSQL immagini          |

---

## 🔗 Comunicazioni Esterne

| Sistema                    | Protocollo | Descrizione                                     |
| :----------------------    | :----------| :---------------------------------------------- |
| **Image-Analyzer-Service** | Kafka      | Analisi immagini safe search, sfruttando la messaggistica e Redis per evitare traffico HTTP    |
| **Frontend**               | REST e WEB SOCKET      | Gestione caricamento, like e visualizzazione live grazie al supporto di WEBSOCKET       |

---

## 🔒 Sicurezza

- **OAuth2 Resource Server** con validazione token JWT.
- Endpoint protetti da autorizzazione basata sui ruoli `USER`, `ADMIN`.
- Comunicazione HTTPS suggerita in produzione.

---

## 🛠️ Setup sviluppo (locale)

1. Avere **PostgreSQL** e **MongoDB** attivi.
2. Avere **Kafka** e **Redis** attivi.
3. Configurare il file `application.yml` o `application.properties`.

Avvio del microservizio:

```bash
mvn clean spring-boot:run
```

Accesso Swagger:

- [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)

> **⚠️ Funziona direttamente se deployato con docker-compose**

> **✅ Se deployato con K8S, eseguire:**
   ```bash
   kubectl port-forward svc/photo-service 8082:8082
   ```

---

## 📃 Endpoints principali

- `POST /api/photos/upload` → Caricamento immagine.
- `GET /api/photos/photo?photoId={id}` → Recupera una foto specifica.
- `GET /api/photos/metadata/user/batch` → Batch metadata foto utente.
- `POST /api/photos/like/add?photoId={id}` → Aggiungi like.
- `DELETE /api/photos/like/remove?photoId={id}` → Rimuovi like.
- `DELETE /api/photos/delete?photoId={id}` → Cancella foto.

**Nota:** Tutti gli endpoint sono documentati automaticamente tramite OpenAPI 3.

---

## 🔥 Dipendenze Maven principali

Estratte dal `pom.xml`:

```xml
<dependency>spring-boot-starter-webflux</dependency>
<dependency>spring-boot-starter-data-jpa</dependency>
<dependency>spring-boot-starter-data-mongodb</dependency>
<dependency>mongodb-driver-sync</dependency>
<dependency>spring-boot-starter-oauth2-resource-server</dependency>
<dependency>spring-boot-starter-actuator</dependency>
<dependency>spring-boot-starter-data-redis</dependency>
<dependency>spring-kafka</dependency>
<dependency>springdoc-openapi-starter-webflux-ui</dependency>
<dependency>springdoc-openapi-starter-webflux-api</dependency>
<dependency>loki-logback-appender</dependency>
<dependency>micrometer-registry-prometheus</dependency>
```

---

## 📊 Monitoring & Health Check

- Health Check via `/actuator/health`
- Metriche Prometheus via `/actuator/prometheus`
- Logging centralizzato con Loki.

---

## 📊 Comunicazione Asincrona & Caching

- **Kafka** utilizzato per inviare eventi di foto caricate all'**Image-Analyzer-Service**.
- **Redis** utilizzato per:
  - Caching dei metadata e immagini.
  - Caching batch ottimizzato per ridurre server load.
  - Serializzazione uniforme JSON per massima efficienza.

- **Event-Driven Flow**
  1. Quando viene effettuato `/upload`, vengono salvati temporaneamenti i riferimenti dei metadati.
  2. L'immagine viene messa in cache come String codificata base 64 e viene pubblicato un messaggio su `photo-uploaded`.
  3. Viene consumato il messaggio su `photo-uploaded`, da quale viene estratto l'id della photo.
  4. Con l'id estratto viene cercata l'immagine dalla cache, estratta, processata ed infine viene pubblicato un messaggio su `photo-processed`.
  5. Viene consumato il messaggio su `photo-processed`, dal quale viene estratto l'id della photo.
  6. Con l'id estratto vengono recuperati i metadati da **Redis**, si ricostruisce l'oggetto completo Photo + Metadati (**PhotoDto**).
  7. Vengono persistite le componenti nei rispettivi database ed una finale operazione di `CacheEvict` per photoId con i vari tag **image :** e **result :**.
  8. `PhotoWebSocketHandler` pubblica una notifica per aggiornare il `front-end`, con **photoId**.

![alt text](image.png)

---

## 📊 Documentazione API

| API                          | JSON                                      |
| :--------------------------  | :----------------------------------       |
| **Swagger UI**               | [photo-service](../docs/api/photo-api.json) |

---

## 👨‍💻 Contributore

- [Mohamed Gabr Ashour](https://github.com/Avalanche-git-dev)

---

# 🌟 Stato

**Completato** — Pronto per ambienti di sviluppo e produzione.
