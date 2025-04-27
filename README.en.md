<p align="right">
  <a href="./README.md">🇮🇹 Italiano</a> |
  <strong>🇬🇧 English</strong>
</p>

# Photo-Dom – Technical Documentation

![Photo-Dom Logo](<Immagine 2025-04-20 133458.png>)

**Photo-Dom** is a social application built from scratch, designed as a management system, created to explore and implement:

### Main Objectives
- **Microservices architecture.**
- **Advanced technology skills.**
- **Scalability, resilience, asynchronous communication, KPI optimization, learn by doing.**

---

## Requirements

- Flexible structure for future additions.
- Horizontal scalability.
- Cost containment.
- Resilient architecture.

---

## Features

- Secure registration/login with roles (Admin/User).
- Photo upload, management, and interaction.
- Real-time likes and comments via WebSocket.
- Admin dashboard for user management.
- Safe-search to ensure content security.

---

## General Networking

### Topology

- Angular frontend via HTTP/WebSocket.
- API Gateway as a single entry point.
- REST and Kafka communications between microservices.
- Dedicated databases: MongoDB, MySQL, PostgreSQL.
- Distributed caching: Redis.
- Security: Keycloak as Identity Provider.

### Diagram

![UML Diagram](PhotoDom-General.png)

---

## Core Concepts

- **Single Responsibility Principle** applied.
- **Event-Driven Architecture** with Kafka.
- **Resilience** with Circuit Breaker (Resilience4J).
- **Advanced security** with OIDC, OAuth2, JWT, and Keycloak.

---

## Technologies Used

### Backend

- **Java 17+** & **Spring Boot 3.3.7**
- **Python 3.10** with **Pillow**, **kafka-python**, and **Redis**
- **Spring Security**
- **MongoDB**, **MySQL**, **Redis**, **Apache Kafka**, **Loki**, **PostgreSQL**

### Frontend

- **Angular 22**
- **Angular Material**
- **RxJS**, **WebSocket**
- **Bootstrap 5.3.3**

---

## Key Features

- Secure user management (Keycloak & OAuth2 with JWT support).
- Role-based access control.
- CRUD operations for photos, comments, likes.
- Additional moderation view for ADMIN.
- Image filtering of unsafe content using **Cloud Vision API** + optimized **Python Service**.
- Centralized logging with **Loki**.
- Live monitoring dashboard with **Grafana**.

---

## Main Folder Structure

| Folder                       | Description                                                           |
| :--------------------------- | :-------------------------------------------------------------------- |
| [`api-gateway/`](api-gateway/README.en.md)               | Central API Gateway (Spring Cloud Gateway)                           |
| [`user-service/`](user-service/README.en.md)             | User management microservice (Java Spring)                           |
| [`photo-service/`](photo-service/README.en.md)           | Photo management microservice (Java Spring)                          |
| [`comment-service/`](comment-service/README.en.md)       | Comment management microservice (Java Spring)                        |
| [`image-analyzer-service/`](image-analyzer-service/README.en.md) | Python microservice for image analysis (Google Vision API)     |
| [`user-provider/`](user-provider/README.en.md)           | Maven module for Keycloak–User-Service integration                   |
| [`front-end/`](front-end/README.en.md)                   | Angular frontend application                                          |
| `k8s/manifests/`             | Kubernetes manifests for infrastructure and services                 |
| `Docker/`                    | Dockerfiles and database configurations                               |
| `setup.ps1`                  | PowerShell script to set up Kind cluster and deploy                   |
| `create-kind-cluster.ps1`    | PowerShell script to create Kind cluster                              |
| `delete-kind-cluster.ps1`    | PowerShell script to delete Kind cluster and clean `/etc/hosts`       |
| `README.en.md`               | Main documentation                                                    |

---

## Internal Communications

- **REST API** for user management and login.
- **Kafka topics** for asynchronous events (photo uploaded, photo analyzed).
- **WebSocket** for real-time notifications.

---

# Local Deployment Guide

## Prerequisites

- Docker Desktop installed.
- Kubernetes enabled.
- Kind installed.
- Node.js and npm installed.

> **⚠️ Kubernetes deploy limited to 1 replica:**  
> Due to resource constraints, all microservices are launched with a single replica in the local Kind cluster.  
> The infrastructure is designed to be deployable in any environment (on-premise or cloud), with a preference for cloud environments to leverage scalability and high availability.

> **⚠️ Docker Compose deploy:**  
> The application can also be deployed using Docker Compose for local testing.  
> You must manually update the frontend environment configuration:  
> `front-end/photo-dom/src/environments/environment.ts`.  
> After that, run:
> ```bash
> docker-compose up -d --build
> ```

---

## Cloud Vision API ⚠️

To enable image analysis:

1. Register on [Google Cloud Vision](https://cloud.google.com/vision).  
2. Open [Google Cloud Console](https://console.cloud.google.com) and create/select a project.  
3. Enable the Cloud Vision API.  
4. Navigate to **IAM & Admin > Service Accounts**.
![alt text](image.png) ![alt text](image-2.png)  
5. Create or select a service account.  
6. Go to **Manage Keys** → **Add Key > Create new key**.  
7. Select **JSON** format and click **Create**. 
![alt text](image-1.png) 
8. Rename the downloaded file to `api-Google.json`.  
9. Copy it into `image-analyzer-service/credentials/`.

> **❗️ Important:** Without this setup, image upload functionality will not work!

---

## Startup Procedure

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Avalanche-git-dev/PhotoDom.git
   cd PhotoDom-MicroServices
   ```

2. **Create the Kind cluster**:
   ```bash
   ./create-kind-cluster.ps1
   ```

3. **Run the complete setup**:
   ```bash
   ./setup.ps1
   ```

4. **Check Pod statuses**:
   ```bash
   kubectl get pods
   ```

5. **Port-forward Keycloak**:
   ```bash
   kubectl port-forward svc/keycloak 8180:8180
   ```

6. **Port-forward API-Gateway**:
   ```bash
   kubectl port-forward svc/api-gateway 8080:8080
   ```

7. **Port-forward Frontend**:
   ```bash
   kubectl port-forward svc/front-end 4200:80
   ```

8. **Access the application**:
   Open your browser and go to [http://localhost:4200](http://localhost:4200).

---

## Utils & Monitoring

1. **Port-forward Grafana**:
   ```bash
   kubectl port-forward svc/grafana 3000:3000
   ```
   → [http://localhost:3000](http://localhost:3000)

2. **Port-forward Kafka-UI**:
   ```bash
   kubectl port-forward svc/kafka-ui 8090:8090
   ```
   → [http://localhost:8090](http://localhost:8090)

3. **Access Keycloak Admin Console**:
   → [http://localhost:8180](http://localhost:8180)

4. **Delete the cluster**:
   ```bash
   ./delete-kind-cluster.ps1
   ```

---

## Deliverables & Testing

### API Documentation

Each service exposes OpenAPI documentation:

| Path                                | Service           |
| :---------------------------------- | :---------------- |
| `/aggregate/user-service/v3/api-docs`   | User Service      |
| `/aggregate/photo-service/v3/api-docs`  | Photo Service     |
| `/aggregate/comment-service/v3/api-docs`| Comment Service   |

Swagger available at:  
- [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

### API Testing

Collections are available in `docs/test/`:

- **User Controller**
- **Admin Controller**
- **Keycloak Controller**
- **Photo Controller**
- **Comment Controller**
- **API Gateway**
- **WebFlow Endpoint**

> **⚠️ Important:**  
> Obtain a valid Bearer token (TokenId or TokenIdAdmin) before accessing secured APIs!

---

# Learning Objectives

- Microservices architecture design.
- Cloud-native application development.
- Secure authentication management.
- Scalability, resilience, distributed logging.
- Synchronous and asynchronous communication.
- Heterogeneous system integration.

---

# Contributor

- [Mohamed Gabr Ashour](https://github.com/Avalanche-git-dev) – **Full-Stack Developer**

---

> **Photo-Dom**: more than just an app – a complete distributed ecosystem for learning and growth.
