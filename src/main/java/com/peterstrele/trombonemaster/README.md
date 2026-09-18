# TromboneMaster

TromboneMaster is a platform for learning, practicing, recording, reviewing, and competing as a trombone player.

The platform combines structured learning, practice tools, performance recording, feedback, and competitions into one system.

## Architecture

TromboneMaster is built as a **DDD-oriented modular monolith**.

The main architectural layers are:

```text
API
 ↓
Application
 ↓
Domain
 ↑
Infrastructure
```

* **API** — REST endpoints and HTTP concerns.
* **Application** — use-case orchestration and application services.
* **Domain** — business rules, aggregates, entities, value objects, and domain services.
* **Infrastructure** — PostgreSQL, external services, AI, video infrastructure, and other technical implementations.

The backend is built with **Java and Spring Boot**.

## API

The REST API follows a **contract-first** approach.

The API contract is defined in:

```text
src/main/resources/openapi/openapi.yaml
```

OpenAPI Generator generates the Java API interfaces from this specification.

The flow is:

```text
openapi.yaml
     ↓
OpenAPI Generator
     ↓
Generated API interface
     ↓
REST Controller
     ↓
Application
     ↓
Domain
     ↓
Infrastructure
```

The API is exposed under:

```text
/api
```

For example:

```text
GET    /api/users
GET    /api/users/{userId}
POST   /api/users
PUT    /api/users/{userId}
DELETE /api/users/{userId}
```

See [API Contract](docs/architecture/api-contract.md) for more information.

## Project Structure

```text
trombone-master/
├── docs/
│   └── architecture/
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   │       ├── application.yaml
│   │       └── openapi/
│   │           └── openapi.yaml
│   └── test/
└── pom.xml
```

## Running Locally

### Requirements

* Java
* Maven
* PostgreSQL

Start the application with:

```bash
mvn spring-boot:run
```

The application runs by default on:

```text
http://localhost:8080
```

Swagger UI is available at:

```text
http://localhost:8080/swagger-ui.html
```

## Generating the API

After changing the OpenAPI specification, regenerate the API interfaces:

```bash
mvn clean generate-sources
```

Generated code is placed under:

```text
target/generated-sources/openapi
```

Generated sources should not be edited manually.

## Documentation

Architecture and development decisions are documented in:

```text
docs/
```

Current documentation:

* [API Contract](docs/architecture/api-contract.md)

More architecture documentation will be added as the system evolves.
