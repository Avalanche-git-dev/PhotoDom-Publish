<p align="right">
  <strong>🇮🇹 Italiano</strong> |
  <a href="./README.en.md">🇬🇧 English</a>
</p>

# Comment-Service

Microservizio parte del progetto **Photo-Dom**, responsabile della **gestione dei commenti**: creazione, gestione, visualizzazione, cancellazione e gestione risposte ai commenti, con aggiornamenti in tempo reale tramite WebSocket.

---

## ✨ Funzionalità principali

- Creazione di commenti su foto.
- Supporto a **commenti principali** e **risposte** (threading).
- Conteggio commenti e risposte.
- Rimozione commenti.
- Esposizione API REST documentate con **Swagger** (OpenAPI 3).
- Aggiornamenti in tempo reale sul frontend tramite **WebSocket**.
- Caching commenti su **Redis** per migliorare velocità di accesso e ridurre carico server.
- Gestione ottimizzata delle richieste batch per massimizzare l'efficienza.

---

## 💾 Struttura Principale

| Cartella/File        | Descrizione                                       |
| :------------------- | :------------------------------------------------ |
| `controller/`         | Gestione delle API HTTP REST per i commenti       |
| `service/`            | Business Logic di gestione commenti e risposte   |
| `repository/`         | Accesso dati reattivo su MongoDB                  |
| `model/`              | Entità di dominio (Commento, Risposte)            |
| `configuration/`      | Configurazione Redis, Sicurezza, WebSocket        |
| `dto/`                | Oggetti di trasferimento dati                    |
| `socket/`             | Gestione WebSocket notifiche commenti live        |

---

## ⚙️ Tecnologie e Librerie Principali

| Tecnologia/Stack              | Ruolo                                |
| :----------------------------- | :---------------------------------- |
| **Spring Boot 3.3.7**          | Framework principale backend       |
| **Spring Security**            | Sicurezza API con JWT               |
| **Spring WebFlux**             | WebServer reattivo                  |
| **Spring Data MongoDB Reactive** | Persistenza commenti su MongoDB     |
| **Redis**                      | Caching commenti e risposte         |
| **Spring Actuator**            | Monitoraggio applicativo           |
| **Loki + Prometheus**          | Logging e metriche centralizzate    |
| **Springdoc OpenAPI 3**        | Documentazione API automatica       |
| **MongoDB**                    | Database NoSQL commenti             |

---

## 🔗 Comunicazioni Esterne

| Sistema       | Protocollo | Descrizione                                  |
| :------------ | :----------| :------------------------------------------- |
| **Frontend**  | REST e WebSocket | Invio e ricezione commenti e notifiche aggiornate live |

---

## 🔒 Sicurezza

- **OAuth2 Resource Server** con validazione token JWT.
- Endpoint protetti con ruoli utente `USER`, `ADMIN`.
- Comunicazione sicura suggerita tramite HTTPS in produzione.

---

## 🛠️ Setup sviluppo (locale)

1. Avere **MongoDB** e **Redis** attivi.
2. Configurare il file `application.yml` o `application.properties`.

Avvio del microservizio:

```bash
mvn clean spring-boot:run
```

Accesso Swagger:

- [http://localhost:8083/swagger-ui.html](http://localhost:8083/swagger-ui.html)

> **⚠️ Funziona direttamente se deployato con docker-compose**

> **✅ Se deployato con K8S, eseguire:**
   ```bash
   kubectl port-forward svc/comment-service 8083:8083
   ```

---

## 💃 Endpoints principali

- `POST /api/comments/comment/add` → Aggiunta nuovo commento o risposta.
- `GET /api/comments/comment?id={id}` → Recupera un commento per ID.
- `GET /api/comments/comment/photo?photoId={id}` → Recupera tutti i commenti di una foto.
- `GET /api/comments/comment/user?userId={id}` → Recupera tutti i commenti di un utente.
- `GET /api/comments/comment/replies?id={id}` → Recupera tutte le risposte a un commento.
- `GET /api/comments/count/comments?photoId={id}` → Conta i commenti principali di una foto.
- `GET /api/comments/count/replies?parentCommentId={id}` → Conta le risposte di un commento.
- `DELETE /api/comments/comment/delete?id={id}` → Cancella un commento.

**Nota:** Tutti gli endpoint sono documentati automaticamente tramite OpenAPI 3.

---

## 🔥 Dipendenze Maven principali

Estratte dal `pom.xml`:

```xml
<dependency>spring-boot-starter-webflux</dependency>
<dependency>spring-boot-starter-data-mongodb-reactive</dependency>
<dependency>spring-boot-starter-data-redis</dependency>
<dependency>spring-boot-starter-oauth2-resource-server</dependency>
<dependency>spring-boot-starter-actuator</dependency>
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

## 📊 Funzionamento in Real-Time & Caching

- **Redis** viene utilizzato per:
  - Caching commenti principali e risposte.
  - Recupero rapido dei dati più richiesti.
- **WebSocket** utilizzato per:
  - Notificare live nuove aggiunte e modifiche di commenti.
  - Gestire aggiornamenti batch evitando carico eccessivo.
- **Ottimizzazione Batch**:
  - Controllo aggiornamenti front-end con messaggi aggregati.
  - Riduzione carico backend e miglioramento esperienza utente.
**Comment Flow:**
1. Quando un commento è aggiunto viene sia persistito che aggiunto in cache. 
2. La persistenza e l'eliminazione dei commenti sfruttano metodi ricorsivi in quanto è stato utilizzato il **Design Pattern** `Composite` per la struttura dei Commenti.
2. `CommentWebSocketHandler` invia notifiche real-time al frontend.
3. In base al limite del batch impostato su front-end, vengono richiamati i commenti che possono essere gia in cache o meno.
---

## 📊 Documentazione API

| API                          | JSON                                      |
| :--------------------------  | :----------------------------------       |
| **Swagger UI**               | [comment-service](../docs/api/comment-api.json) |

---

## 👨‍💻 Contributore

- [Mohamed Gabr Ashour](https://github.com/Avalanche-git-dev)

---

# 🌟 Stato

**Completato** — Pronto per ambienti di sviluppo e produzione.
