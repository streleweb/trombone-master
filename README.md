# 🎺 TromboneMaster

TromboneMaster is a platform for trombone players focused on structured learning, masterclasses, practice, and eventually intelligent feedback on playing.

The project is currently under active development, with the backend being built first using Spring Boot, PostgreSQL, OpenAPI, and Domain-Driven Design principles.

## Vision

The goal is to build a complete learning and practice platform for trombone players.

The platform is planned to include:

* structured trombone **masterclasses**
* lessons, exercises, and practice material
* masterclass content manageable through an **admin dashboard**
* user accounts and progress tracking
* recording and analysis of a player's trombone playing
* AI-assisted feedback on areas such as intonation, rhythm, tone, articulation, and consistency
* a shared backend for multiple client applications

The frontend is planned around **Quasar**, allowing TromboneMaster to target:

* Web
* Android
* iOS
* Windows
* macOS

The long-term goal is for the API to remain the central application backend regardless of which client is being used.

---

## Architecture

The backend is designed around **Domain-Driven Design (DDD)** with a ports-and-adapters style separation between the domain and infrastructure.

```text
                         ┌──────────────────────────────┐
                         │          REST API            │
                         │                              │
                         │ Controllers                  │
                         │ OpenAPI generated contracts  │
                         │ Request / Response mapping   │
                         └──────────────┬───────────────┘
                                        │
                                        ▼
                         ┌──────────────────────────────┐
                         │      Application Layer       │
                         │                              │
                         │ Command Services             │
                         │ Query Services               │
                         │ Commands / Results           │
                         │ Outbound Ports               │
                         └──────────────┬───────────────┘
                                        │
                                        ▼
                         ┌──────────────────────────────┐
                         │         Domain Layer         │
                         │                              │
                         │ Aggregates                   │
                         │ Entities                     │
                         │ Value Objects                │
                         │ Domain Rules                 │
                         └──────────────┬───────────────┘
                                        │
                              outbound interfaces
                                        │
                                        ▼
                         ┌──────────────────────────────┐
                         │     Infrastructure Layer     │
                         │                              │
                         │ JPA Repository Adapters      │
                         │ PostgreSQL Entities          │
                         │ Password Hashing             │
                         │ Security                     │
                         └──────────────┬───────────────┘
                                        │
                                        ▼
                                ┌──────────────┐
                                │ PostgreSQL   │
                                └──────────────┘
```

A core design goal is that the **domain does not depend on Spring, JPA, PostgreSQL, HTTP, or other infrastructure concerns**.

For example:

```text
UserController
      │
      ▼
UserCommandService
      │
      ▼
User Aggregate
      │
      ▼
UserRepository            ← application port
      ▲
      │ implements
      │
JpaUserRepository         ← infrastructure adapter
      │
      ▼
Spring Data JPA
      │
      ▼
PostgreSQL
```

This keeps business logic separated from how the application is exposed and how its data is persisted.

---

## Domain Model

The domain layer is intentionally separated from the persistence model.

A domain `User`, for example, should not become a JPA entity simply because users happen to be stored in PostgreSQL.

```text
Domain                         Infrastructure

User                           UserEntity
UserId                         JPA
Domain behavior       <---->   UserEntityMapper
                               PostgreSQL
```

Mapping between these representations happens at the infrastructure boundary.

### Planned Domain Refactoring

The current domain model is **not considered finished**.

Some domain concepts are still represented using primitive Java types:

```java
String username;
String email;
String displayName;
String country;
```

As the domain matures, these will be refactored into proper **Value Objects**:

```text
User
 ├── UserId
 ├── Username
 ├── Email
 ├── DisplayName
 └── CountryCode
```

Instead of allowing primitive values to carry domain meaning:

```java
String email;
```

the domain should eventually express that meaning explicitly:

```java
Email email;
```

Validation, normalization, equality, and invariants can then belong to the corresponding Value Object rather than being scattered across aggregates and services.

The same principle will be applied as the domain grows: concepts with identity should become proper **Entities**, concepts defined by their values should become **Value Objects**, and consistency boundaries should be represented by **Aggregates**.

The goal is not simply to organize packages according to DDD terminology, but to gradually develop a domain model that actually expresses the TromboneMaster domain.

---

## REST API

TromboneMaster follows an **API-first** approach using OpenAPI 3.1.

```text
OpenAPI Contract
       │
       ▼
Generated API interfaces / models
       │
       ▼
REST implementation
       │
       ▼
Application / Domain
```

### Authentication

| Method | Endpoint             | Description                    |
| ------ | -------------------- | ------------------------------ |
| `POST` | `/api/auth/register` | Register a new user            |
| `POST` | `/api/auth/login`    | Authenticate and obtain tokens |
| `POST` | `/api/auth/refresh`  | Refresh an access token        |
| `POST` | `/api/auth/logout`   | Revoke a refresh token         |

### Current User

| Method   | Endpoint        | Description                             |
| -------- | --------------- | --------------------------------------- |
| `GET`    | `/api/users/me` | Get the authenticated user              |
| `PUT`    | `/api/users/me` | Update the authenticated user           |
| `DELETE` | `/api/users/me` | Delete the authenticated user's account |

### User Management

| Method   | Endpoint              | Description               |
| -------- | --------------------- | ------------------------- |
| `GET`    | `/api/users`          | Retrieve and filter users |
| `GET`    | `/api/users/{userId}` | Retrieve a user by ID     |
| `PUT`    | `/api/users/{userId}` | Update a user             |
| `DELETE` | `/api/users/{userId}` | Delete a user             |

`GET /api/users` supports pagination and filtering by username, email, display name, and country.

Protected endpoints are designed around JWT bearer authentication:

```http
Authorization: Bearer <access-token>
```

The API contract already defines the authentication flow, while parts such as login, JWT issuance, refresh-token handling, logout, and authorization are still being implemented incrementally.

Detailed request schemas, validation constraints, responses, status codes, and authentication requirements are documented in:

```text
docs/api-contract.md
```

The **OpenAPI specification remains the source of truth** for the public API.

---

## Technology

The backend currently uses:

* Java
* Spring Boot
* Spring Security
* Spring Data JPA
* PostgreSQL
* OpenAPI 3.1 / OpenAPI Generator
* Maven
* JUnit / MockMvc
* Testcontainers
* Docker

Integration tests run against a real PostgreSQL container rather than replacing PostgreSQL with an in-memory database.

Run the complete build and test suite with:

```bash
./mvnw clean verify
```

Docker must be running for the Testcontainers-based integration tests.

---

## Project Status

TromboneMaster is under active development.

The current focus is establishing a strong foundation around the API contract, authentication, domain model, persistence, and integration testing.

From there, the project can grow toward masterclasses, practice features, progress tracking, audio analysis, and intelligent feedback while keeping the core domain independent from the technologies surrounding it.

---

🎺 **TromboneMaster — built for better practice.**
