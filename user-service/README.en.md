<p align="right">
  <a href="./README.md">🇮🇹 Italiano</a> |
  <strong>🇬🇧 English</strong>
</p>

# User-Service

Microservice part of the **Photo-Dom** project, responsible for **user management**: registration, authentication, role management, and integration with **Keycloak** authentication system.

---

## ✨ Main Features

- Secure user registration.
- User authentication via Keycloak (Resource Server JWT).
- Integration with **Keycloak** through [**`user-provider`**](../user-provider/README.md) module.
- Personal profile CRUD.
- Promotion, banning, and status management of users.
- Interaction through **WebSocket** (e.g., notifications).
- REST API exposure documented with **Swagger** (OpenAPI 3).
- Asynchronous communication via **Kafka**.

---

## 💾 Main Structure

| Folder/File         | Description                                 |
| :------------------ | :----------------------------------------- |
| `controller/`        | Management of HTTP REST APIs              |
| `service/`           | Application Business Logic                |
| `repository/`        | Data access through Spring Data JPA       |
| `model/`             | Domain entities (User, Role, etc.)        |
| `configuration/`     | Security and WebSocket configuration      |
| `dto/`               | Data Transfer Objects between layers      |
| `socket/`            | SocketHandler                             |
| `entity/`            | Hibernate database entity mappings        |
| `filter/`            | GlobalExceptionHandler                    |

---

## ⚙️ Main Technologies and Libraries

| Technology/Stack        | Role                                |
| :---------------------- | :--------------------------------- |
| **Spring Boot 3.3.7**    | Main backend framework            |
| **Spring Security**      | OAuth2 token management            |
| **Spring Data JPA**      | Persistence with MySQL             |
| **Spring WebSocket**     | Real-time communication            |
| **Spring Actuator**      | Application monitoring             |
| **Loki + Prometheus**    | Centralized logging and metrics    |
| **Springdoc OpenAPI 3**  | API auto-documentation             |
| **MySQL 8**              | Relational Database                |

---

## 🔗 External Communications

| System           | Protocol | Description                              |
| :--------------- | :-------- | :-------------------------------------- |
| **Keycloak**      | REST      | OAuth2 Resource Server Authentication   |
| **Frontend**      | WebSocket | Real-time notifications to client      |

---

## 🔒 Security

- **OAuth2 Resource Server**: JWT tokens validation issued by Keycloak.
- Endpoints protected by route separation and Filter Chain based on `USER` and `ADMIN` roles.
- Authentication and authorization managed through [Security Filter Chain](src/main/java/com/app/userservice/configuration/SecurityConfig.java) orchestrated by **Spring Security**.
- HTTPS communication recommended in production environments.

---

## 🛠️ Local Development Setup

1. Have **MySQL** running.
2. Have **Keycloak** running with configured Realm and Client.
3. Configure `application.yml` or `application.properties` as needed (e.g., DB host, realm).

Start the microservice:

```bash
mvn clean spring-boot:run
```

Access Swagger:

- [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)

> **⚠️ Works directly if deployed with docker-compose**

> **✅ If deployed with K8S, execute the following command:**
   ```bash
   kubectl port-forward svc/user-service 8081:8081
   ```

---

## 📜 Main Endpoints

- `POST /api/users/register` → Register a new user.
- `POST /keycloak/login` → Login (JWT token request).
- `GET /api/users/profile` → View user profile.
- `PUT /api/users/profile/update` → Update user profile.
- `PUT /api/users/credentials/update` → Change password.
- `POST /api/admins/promote/admin?id={id}` → Promote user to Admin.
- `POST /api/admins/ban/add?id={id}` → Ban a user.
- `POST /api/admins/ban/remove?id={id}` → Unban a user.

**Note:** All endpoints are automatically documented with OpenAPI 3.

---

## 🔥 Main Maven Dependencies

Extracted from `pom.xml`:

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

- Health Check at `/actuator/health`
- Prometheus Metrics at `/actuator/prometheus`
- Centralized logging with Loki.

---

## 📈 API Documentation

| API                         | JSON                                      |
| :-------------------------- | :--------------------------------------- |
| **Swagger UI**               | [user-service](../docs/api/user-api.json) |

---

## 👨‍💼 Contributor

- [Mohamed Gabr Ashour](https://github.com/Avalanche-git-dev)

---

# 🌟 Status

**Completed** — Ready for development and production environments.
