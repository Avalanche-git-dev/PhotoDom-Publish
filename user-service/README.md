# User-Service

Microservizio parte del progetto **Photo-Dom**, responsabile della **gestione degli utenti**: registrazione, autenticazione, gestione ruoli e integrazione con il sistema di autenticazione Keycloak.

---

## ✨ Funzionalità principali

- Registrazione sicura utenti.
- Autenticazione utenti tramite Keycloak (Resource Server JWT).
- Integrazione con **Keycloak** tramite modulo [**`user-provider.`**](..\user-provider\README.md)
- CRUD profilo personale.
- Promozione, bannaggio e gestione stato utente.
- Interazione tramite **WebSocket** (es. notifiche).
- Esposizione API REST documentate con **Swagger** (OpenAPI 3).
- Comunicazione asincrona tramite **Kafka**.

---

## 💾 Struttura Principale

| Cartella/File        | Descrizione                                        |
| :------------------- | :------------------------------------------------- |
| `controller/`         | Gestione delle API HTTP REST                      |
| `service/`            | Business Logic dell'applicativo                   |
| `repository/`         | Accesso ai dati tramite Spring Data JPA           |
| `model/`              | Entità del dominio (User, Role, ecc.)             |
| `configuration/`      | Configurazioni di sicurezza e WebSocket           |
| `dto/`                | Oggetti di trasferimento dati tra i layer         |
| `socket/`             | SocketHandler        |
| `entity/`             | Entità da mappare sul DB per Hibernate            |
| `filter/`             | GlobalExceptionHandler        |

---

## ⚙️ Tecnologie e Librerie Principali

| Tecnologia/Stack        | Ruolo                            |
| :---------------------- | :------------------------------- |
| **Spring Boot 3.3.7**    | Framework principale backend     |
| **Spring Security**      | Sicurezza e gestione token OAuth2 |
| **Spring Data JPA**      | Persistenza dati su MySQL         |
| **Spring WebSocket**     | Comunicazione in tempo reale      |
| **Spring Actuator**      | Monitoraggio applicativo         |
| **Loki + Prometheus**    | Logging e metriche centralizzate |
| **Springdoc OpenAPI 3**  | Documentazione API automatica    |
| **MySQL 8**              | Database relazionale             |

---

## 🔗 Comunicazioni Esterne

| Sistema        | Protocollo | Descrizione                            |
| :------------  | :----------| :------------------------------------- |
| **Keycloak**    | REST       | Autenticazione OAuth2 Resource Server |
| **Frontend**    | WebSocket  | Invio notifiche live                   |

---

## 🔒 Sicurezza

- **OAuth2 Resource Server**: validazione dei token JWT emessi da Keycloak.
- Endpoint protetti tramite separazione delle **route** e protezione nel Filter Chain basato sui ruoli `USER`, `ADMIN`.
- Autenticazioni e autorizzazioni gestite tramite una sequenza orchestrate di [Security Filter Chain](src/main/java/com/app/userservice/configuration/SecurityConfig.java) forniti da **Spring Security**.
- Comunicazione HTTPS suggerita per ambienti di produzione.

---

## 🛠️ Setup sviluppo (locale)

1. Avere **MySQL** attivo.
2. Avere **Keycloak** attivo con Realm e Client configurati.
3. Configurare il file `application.yml` o `application.properties` se necessario (es: host DB, realm).

Avvio del microservizio:

```bash
mvn clean spring-boot:run
```

Accesso Swagger:

- [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)

> **⚠️ Funziona direttamente solo se deployato con docker-compose**  

> **✅ Se deployato con K8S, basta eseguire il seguente comando :**
   ```bash
   kubectl port-forward svc/user-service 8081:8081
   ```
---

## 📃 Endpoints principali

- `POST /api/users/register` → Registrazione nuovo utente.
- `POST /keycloak/login` → Login (richiesta token JWT).
- `GET /api/users/profile` → Visualizza dati profilo.
- `PUT /api/users/profile/update` → Aggiorna dati profilo.
- `PUT /api/users/credentials/update` → Cambia password.
- `POST /api/admins/promote/admin?id={id}` → Promuove utente ad Admin.
- `POST /api/admins/ban/add?id={id}` → Bana utente.
- `POST /api/admins/ban/remove?id={id}` → Rimuove ban ad utente.

**Nota:** Tutti gli endpoint sono documentati automaticamente con OpenAPI 3.

---

## 🔥 Dipendenze Maven principali

Estratte dal `pom.xml`:

```xml
<dependency>spring-boot-starter-web</dependency>
<dependency>spring-boot-starter-data-jpa</dependency>
<dependency>spring-boot-starter-security</dependency>
<dependency>spring-boot-starter-oauth2-resource-server</dependency>
<dependency>spring-boot-starter-validation</dependency>
<dependency>spring-boot-starter-actuator</dependency>
<dependency>spring-boot-starter-websocket</dependency>
<dependency>springdoc-openapi-starter-webmvc-ui</dependency>
<dependency>springdoc-openapi-starter-webmvc-api</dependency>
<dependency>mysql-connector-j</dependency>
<dependency>loki-logback-appender</dependency>
<dependency>micrometer-registry-prometheus</dependency>
```

---

## 📊 Monitoring & Health Check

- Health Check via `/actuator/health`
- Metriche Prometheus via `/actuator/prometheus`
- Logging centralizzato con Loki.

---

## 📈 Documentazione API

| API                          | JSON                                      |
| :--------------------------  | :----------------------------------       |
| **Swagger UI**               | [user-service](..\docs\api\user-api.json) |


---

## 👨‍💻 Contributore

- [Mohamed Gabr Ashour](https://github.com/Avalanche-git-dev)

---

# 🌟 Stato

**Completato** — Pronto per ambienti di sviluppo e produzione.
