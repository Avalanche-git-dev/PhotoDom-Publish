<p align="right">
  <strong>🇮🇹 Italiano</strong> |
  <a href="./README.md">🇬🇧 English</a>
</p>

# API-Gateway

Microservice part of the **Photo-Dom** project, responsible for centralized routing between clients and microservices.  
Based on **Spring Cloud Gateway**, it implements resilience through **Circuit Breaker** (Resilience4j) and centralizes security, logging, and monitoring.

---

## ✨ Main Features

- Central API Gateway for all microservices.
- Dynamic routing via **Spring Cloud Gateway**.
- **Circuit Breaker** with automatic fallback for each service.
- Integration with **Spring Security** and protection via OAuth2 JWT.
- Aggregated exposure of OpenAPI documentation from all microservices.
- Centralized logging with **Loki** and metrics collected by **Prometheus**.

---

## 💾 Main Structure

| Folder/File           | Description                               |
| :-------------------- | :--------------------------------------- |
| `configuration/`       | Routing, security, and CORS configuration |
| `RoutesConfiguration`  | Definition of routes and Circuit Breakers |
| `SecurityConfig`       | OAuth2 and CORS permissions configuration |

---

## ⚙️ Main Technologies and Libraries

| Technology/Stack                  | Purpose                             |
| :--------------------------------- | :---------------------------------- |
| **Spring Boot 3.3.7**              | Main backend framework             |
| **Spring Cloud Gateway**           | API routing management             |
| **Spring Cloud Circuit Breaker**   | Resilience and fallback handling   |
| **Spring Security OAuth2**         | JWT token security                 |
| **Loki + Prometheus**              | Centralized logging and monitoring |
| **Springdoc OpenAPI 3 (WebFlux)**  | API documentation aggregation      |

---

## 🔗 External Communications

| System             | Protocol | Description                       |
| :----------------- | :-------- | :-------------------------------- |
| **User-Service**    | REST      | User and profile management       |
| **Photo-Service**   | REST + WS | Photo management and live updates |
| **Comment-Service** | REST + WS | Comment management and live updates|

---

## 🔒 Security

- Integrated **OAuth2 Resource Server**.
- JWT validated through Keycloak Authorization Server.
- Open access only configured for public endpoints (login, registration, Swagger, Prometheus).
- WebSocket protection enabled.
- **CORS** policies configured for frontend at `localhost:4200`.

---

## 🛠️ Local Development Setup

1. Ensure **Keycloak** is active for OAuth2.
2. Backend microservices must be running.
3. Configure the `application.yml` file.

Launch the microservice:

```bash
mvn clean spring-boot:run
