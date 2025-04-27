<p align="right">
  <strong>🇬🇧 English</strong> |
  <a href="./README.md">🇮🇹 Italiano</a>
</p>

# Comment-Service

Microservice part of the **Photo-Dom** project, responsible for **managing comments and replies** related to uploaded photos.

---

## ✨ Main Features

- Add, retrieve, and delete comments.
- Support for replies to comments (nested comments).
- Batch retrieval of comments for photos or users.
- Real-time notifications via **WebSocket**.
- Optimized caching with **Redis**.
- Exposed REST API documented with **Swagger** (OpenAPI 3).

---

## 💾 Main Structure

| Folder/File            | Description                                     |
| :--------------------- | :---------------------------------------------- |
| `controller/`           | Management of REST HTTP APIs                   |
| `service/`              | Application Business Logic                     |
| `repository/`           | Reactive access to MongoDB                     |
| `model/`                | Domain Entities (Comment, Reply, etc.)          |
| `configuration/`        | Security, Redis configuration                  |
| `dto/`                  | Data Transfer Objects between layers           |
| `socket/`               | WebSocket Handler for real-time updates        |

---

## ⚙️ Core Technologies and Libraries

| Technology/Stack                 | Role                                     |
| :-------------------------------- | :--------------------------------------- |
| **Spring Boot 3.3.7**             | Main backend framework                  |
| **Spring Security**               | API Security with JWT                  |
| **Spring WebFlux**                | Reactive Web Server                     |
| **Spring Data MongoDB Reactive**  | Reactive persistence on MongoDB         |
| **Redis**                         | Optimized caching for batch/single read |
| **Spring Actuator**               | Application monitoring                  |
| **Loki + Prometheus**             | Centralized logging and metrics         |
| **Springdoc OpenAPI 3**           | Automatic API documentation             |
| **MongoDB**                       | NoSQL database for comments             |

---

## 🔗 External Communications

| System           | Protocol | Description                            |
| :--------------- | :-------- | :------------------------------------- |
| **Frontend**     | REST & WebSocket | Management of comment actions and real-time updates |

---

## 🔐 Security

- **OAuth2 Resource Server** with JWT token validation.
- Endpoints protected with role-based authorization `USER`, `ADMIN`.
- HTTPS communication recommended for production.

---

## 🛠️ Local Development Setup

1. Ensure **MongoDB** and **Redis** are running.
2. Configure `application.yml` or `application.properties` as needed.

Start the microservice:

```bash
mvn clean spring-boot:run
```

Access Swagger:

- [http://localhost:8083/swagger-ui.html](http://localhost:8083/swagger-ui.html)

> **⚠️ Works directly if deployed with docker-compose**

> **✅ If deployed with K8S, execute:**
   ```bash
   kubectl port-forward svc/comment-service 8083:8083
   ```

---

## 📃 Main Endpoints

- `POST /api/comments/comment/add` → Add a comment.
- `GET /api/comments/comment?id={id}` → Retrieve a specific comment.
- `GET /api/comments/comment/photo?photoId={photoId}` → Retrieve comments for a specific photo.
- `GET /api/comments/comment/user?userId={userId}` → Retrieve comments made by a user.
- `GET /api/comments/comment/replies?id={commentId}` → Retrieve replies to a comment.
- `DELETE /api/comments/comment/delete?id={id}` → Delete a comment.

**Note:** All endpoints are automatically documented with OpenAPI 3.

---

## 🔥 Main Maven Dependencies

Extracted from `pom.xml`:

```xml
<dependency>spring-boot-starter-webflux</dependency>
<dependency>spring-boot-starter-data-mongodb-reactive</dependency>
<dependency>spring-boot-starter-oauth2-resource-server</dependency>
<dependency>spring-boot-starter-actuator</dependency>
<dependency>spring-boot-starter-data-redis</dependency>
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

## 🔍 Real-Time Communication and Caching

- **Redis** used for:
  - Caching comments and replies.
  - Caching batch optimized to reduce server load.
  - Uniform JSON serialization for maximum efficiency.
- **WebSocket** used for:
  - Real-time notifications when new comments or replies are added.
  - Controlled batch updates to provide a seamless user experience.

**Comment Flow:**
1. When a comment or reply is added, it is cached and stored in MongoDB.
2. `CommentWebSocketHandler` sends a real-time update to the frontend.
3. Periodic updates refresh batch comments without overloading the backend.



---

## 🔊 API Documentation

| API                          | JSON                                      |
| :--------------------------  | :----------------------------------       |
| **Swagger UI**               | [comment-service](../docs/api/comment-api.json) |

---

## 👨‍💼 Contributor

- [Mohamed Gabr Ashour](https://github.com/Avalanche-git-dev)

---

# 🌟 Status

**Completed** — Ready for development and production environments.
