<p align="right">
  <strong>🇮🇹 Italiano</strong> |
  <a href="./README.en.md">🇬🇧 English</a>
</p>

# API-Gateway

Microservizio parte del progetto **Photo-Dom**, responsabile dell'instradamento centralizzato delle richieste tra client e microservizi.
Basato su **Spring Cloud Gateway**, implementa resilienza tramite **Circuit Breaker** (Resilience4j) e centralizza sicurezza, logging e monitoraggio.

---

## ✨ Funzionalità principali

- Gateway API centrale per tutti i microservizi.
- Routing dinamico tramite **Spring Cloud Gateway**.
- **Circuit Breaker** con fallback automatico per ogni servizio.
- Integrazione con **Spring Security** e protezione tramite OAuth2 JWT.
- Esposizione aggregata della documentazione OpenAPI di tutti i microservizi.
- Logging centralizzato tramite **Loki** e metriche raccolte da **Prometheus**.

---

## 💾 Struttura Principale

| Cartella/File        | Descrizione                                    |
| :------------------- | :--------------------------------------------- |
| `configuration/`     | Configurazioni di routing, sicurezza e CORS   |
| `RoutesConfiguration`| Definizione delle rotte e Circuit Breaker     |
| `SecurityConfig`     | Configurazione OAuth2 e permessi CORS         |

---

## ⚙️ Tecnologie e Librerie Principali

| Tecnologia/Stack                 | Ruolo                                     |
| :-------------------------------- | :---------------------------------------- |
| **Spring Boot 3.3.7**             | Framework principale backend             |
| **Spring Cloud Gateway**          | Gestione routing API                     |
| **Spring Cloud Circuit Breaker**  | Implementazione resilienza con fallback  |
| **Spring Security OAuth2**        | Sicurezza con JWT Token                  |
| **Loki + Prometheus**             | Logging e metriche centralizzate         |
| **Springdoc OpenAPI 3 (WebFlux)** | Aggregazione documentazione API          |

---

## 🔗 Comunicazioni Esterne

| Sistema            | Protocollo | Descrizione                          |
| :----------------- | :--------- | :----------------------------------- |
| **User-Service**    | REST       | Gestione utenti e profili           |
| **Photo-Service**   | REST + WS  | Gestione foto e websocket live      |
| **Comment-Service** | REST + WS  | Gestione commenti e websocket live  |

---

## 🔒 Sicurezza

- **OAuth2 Resource Server** integrato.
- JWT validati tramite Keycloak come Authorization Server.
- Accesso libero configurato solo per endpoint pubblici (login, registrazione, Swagger, Prometheus).
- Protezione WebSocket.
- Politiche **CORS** configurate per frontend su `localhost:4200`.

---

## 🛠️ Setup sviluppo (locale)

1. Avere **Keycloak** attivo per OAuth2.
2. Assicurarsi che i microservizi backend siano up.
3. Configurare il file `application.yml`.

Avvio del microservizio:

```bash
mvn clean spring-boot:run
```

Accesso Gateway:

- [http://localhost:8080](http://localhost:8080)

> **⚠️** Funziona direttamente con **Docker-Compose** ( o su **Kubernetes** seguendo la normale guida per il deploy ).

---

## 💃 Routing Principale e Fallback

- `/api/users/**`, `/keycloak/**`, `/api/admins/**` ➜ **User-Service**
- `/api/photos/**`, `/ws/photos/**`, `/ws/like/status/**` ➜ **Photo-Service**
- `/api/comments/**`, `/ws/comments/**` ➜ **Comment-Service**
- `/aggregate/**` ➜ Aggregazione OpenAPI per Swagger UI

In caso di errori temporanei:
- Viene attivato il **CircuitBreaker**.
- Viene restituito fallback automatico con errore `503 Service Unavailable` e messaggio dedicato.

---

## 🔥 Dipendenze Maven principali

Estratte dal `pom.xml`:

```xml
<dependency>spring-cloud-starter-gateway</dependency>
<dependency>spring-cloud-starter-circuitbreaker-reactor-resilience4j</dependency>
<dependency>spring-boot-starter-oauth2-resource-server</dependency>
<dependency>spring-boot-starter-actuator</dependency>
<dependency>springdoc-openapi-starter-webflux-ui</dependency>
<dependency>springdoc-openapi-starter-webflux-api</dependency>
<dependency>loki-logback-appender</dependency>
<dependency>micrometer-registry-prometheus</dependency>
```

---

## 📊 Monitoring & Health Check

- Stato microservizi monitorabile tramite Prometheus.
- Logging e tracciamento centralizzato in Loki.
- Health check via `/actuator/health` e `/actuator/prometheus`.
- Dashboard Grafana aggregata sull'intera architettura.

---

## 📊 Documentazione API Aggregata

Accedi da:

- [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

I documenti dei singoli microservizi sono disponibili su `/aggregate/`:

| API Doc Path                           | Microservizio    |
| :------------------------------------- | :--------------- |
| `/aggregate/user-service/v3/api-docs`   | User Service     |
| `/aggregate/photo-service/v3/api-docs`  | Photo Service    |
| `/aggregate/comment-service/v3/api-docs`| Comment Service  |

---

## 👨‍💻 Contributore

- [Mohamed Gabr Ashour](https://github.com/Avalanche-git-dev)

---

# 🌟 Stato

**Completato** — Pronto per ambienti di sviluppo e produzione.
