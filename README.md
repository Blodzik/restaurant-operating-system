# Restaurant OS: Microservices Architecture

An event-driven, microservices-based restaurant operating system designed to handle the entire lifecycle of a modern restaurant—from menu management and staff authentication to order processing and kitchen ticketing.

## 🚀 Tech Stack
* **Language:** Java 21
* **Framework:** Spring Boot 4.1.0 (Web, Data JPA, Validation)
* **Database:** MySQL 8 with Flyway for schema migrations
* **Testing:** JUnit 5, Mockito, Testcontainers (for real database integration testing)
* **Containerization:** Docker & multi-stage Dockerfiles
* **Documentation:** OpenAPI 3 / Swagger UI

## 🗺️ Project Roadmap

This project is actively under development, following a domain-driven design approach.

- [x] **Menu Service:** Manages categories, menu items, and modifiers. Features thread-safe, atomic stock decrementing with database-level row locking to prevent race conditions during high-volume ordering.
- [ ] **Identity Service:** JWT-based staff authentication, PIN logins, and role management (Waiter, Kitchen, Manager).
- [ ] **Floor Service:** Table state machine (`FREE` -> `OCCUPIED` -> `DIRTY`) and zone management.
- [ ] **Order Service:** Handles order creation, cart management, and publishes events via RabbitMQ.
- [ ] **Kitchen Service:** Consumes order events for real-time kitchen ticketing and preparation tracking.
- [ ] **Billing Service:** Handles receipt generation, bill splitting, and payment processing.

## ⚙️ Getting Started (Menu Service)

Currently, the Menu Service is fully operational. To run it locally:

**1. Start the Database**
Spin up the local MySQL development environment using Docker Compose:
```bash
docker-compose up -d