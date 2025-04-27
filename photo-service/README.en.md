<p align="right">
  <a href="./README.md">🇮🇹 Italiano</a> |
  <strong>🇬🇧 English</strong>
</p>

# Photo-Service

Microservice part of the **Photo-Dom** project, responsible for **photo management**: upload, metadata management, likes management, asynchronous communication with image analysis services.

---

## ✨ Main Features

- Secure photo upload.
- Storage of images in MongoDB (**GridFS**) and metadata in PostgreSQL.
- Likes management and aggregated data.
- REST APIs documented with **Swagger** (OpenAPI 3).
- Asynchronous communication with **Image-Analyzer-Service** via **Kafka**.
- Optimized caching on **Redis** for both batch and single requests.
- Server load optimization through uniform data serialization.

---

## 💾 Main Structure

| Folder/File         | Description                                        |
| :------------------- | :------------------------------------------------- |
| `controller/`         | HTTP REST API management                          |
| `service/`            | Business Logic of the application                 |
| `repository/`         | Data access via JPA/PostgreSQL and MongoDB        |
| `model/`              | Domain entities (Photo, Metadata, etc.)           |
| `configuration/`      | Kafka, Mongo, Redis, Security configuration       |
| `dto/`                | Data Transfer Objects between layers             |
| `kafka/`              | Kafka Consumer/Producer and Event Management      |
| `socket/`             | SOCKET HANDLER for Like and Photo operations      |

---

## ⚙️ Main Technologies and Libraries

| Technology/Stack                | Role                              |
| :------------------------------ | :-------------------------------- |
| **Spring Boot 3.3.7**            | Main backend framework            |
| **Spring Security**              | API security with JWT             |
| **Spring WebFlux**               | Reactive WebServer                |
| **Spring Data JPA**              | Metadata persistence on PostgreSQL|
| **Spring Data MongoDB + GridFS** | Binary image storage              |
| **Kafka**                        | Asynchronous communication        |
| **Redis**                        | Optimized caching batch/single    |
| **Spring Actuator**              | Application monitoring            |
| **Loki + Prometheus**            | Centralized logging and metrics   |
| **Springdoc OpenAPI 3**          | Automatic API documentation       |
| **PostgreSQL**                   | Relational database               |
| **MongoDB**                      | NoSQL database for images         |

---

## 🔗 External Communications

| System                    | Protocol | Description                                        |
| :----------------------    | :----------| :------------------------------------------------ |
| **Image-Analyzer-Service** | Kafka      | Image analysis safe search, using messaging and Redis to avoid HTTP traffic |
| **Frontend**               | REST and WebSocket | Upload, like management, and live visualization using WebSocket |

---

## 🔒 Security

- **OAuth2 Resource Server** with JWT token validation.
- Endpoints protected by role-based authorization (`USER`, `ADMIN`).
- HTTPS communication recommended in production.

---

## 🛠️ Development Setup (Local)

1. Have **PostgreSQL** and **MongoDB** running.
2. Have **Kafka** and **Redis** running.
3. Configure the `application.yml` or `application.properties` file.

Microservice startup:

```bash
mvn clean spring-boot:run
```

Swagger Access:

- [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)

> **⚠️ Works directly if deployed with docker-compose**

> **✅ If deployed with K8S, execute:**
   ```bash
   kubectl port-forward svc/photo-service 8082:8082
   ```

---

## 📃 Main Endpoints

- `POST /api/photos/upload` → Upload image.
- `GET /api/photos/photo?photoId={id}` → Retrieve a specific photo.
- `GET /api/photos/metadata/user/batch` → Batch metadata for user photos.
- `POST /api/photos/like/add?photoId={id}` → Add like.
- `DELETE /api/photos/like/remove?photoId={id}` → Remove like.
- `DELETE /api/photos/delete?photoId={id}` → Delete photo.

**Note:** All endpoints are automatically documented with OpenAPI 3.

---

## 🔥 Main Maven Dependencies

Extracted from `pom.xml`:

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
- Prometheus metrics via `/actuator/prometheus`
- Centralized logging with Loki.

---

## 📊 Asynchronous Communication & Caching

- **Kafka** used to send uploaded photo events to **Image-Analyzer-Service**.
- **Redis** used for:
  - Metadata and image caching.
  - Optimized batch caching to reduce server load.
  - Uniform JSON serialization for maximum efficiency.

- **Event-Driven Flow**
  1. When `/upload` is called, metadata references are temporarily saved.
  2. The image is cached as a Base64 encoded string and a message is published to `photo-uploaded`.
  3. The `photo-uploaded` message is consumed, extracting the photo ID.
  4. With the extracted ID, the image is fetched from Redis, processed, and a message is published to `photo-processed`.
  5. The `photo-processed` message is consumed, extracting the photo ID.
  6. Metadata is retrieved from Redis and the complete Photo + Metadata object (**PhotoDto**) is reconstructed.
  7. Components are persisted in the respective databases and a final CacheEvict operation is performed.
  8. `PhotoWebSocketHandler` publishes a live update notification with the **photoId**.

![alt text](image.png)

---

## 📊 API Documentation

| API                          | JSON                                      |
| :--------------------------  | :----------------------------------       |
| **Swagger UI**               | [photo-service](../docs/api/photo-api.json) |

---

## 👨‍💻 Contributor

- [Mohamed Gabr Ashour](https://github.com/Avalanche-git-dev)

---

# 🌟 Status

**Completed** — Ready for development and production environments.
