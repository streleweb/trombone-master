# API Contract and Code Generation

TromboneMaster uses a **contract-first API design**.

The REST API is defined in an OpenAPI YAML document. The YAML file is the source of truth for the API contract. Java API interfaces are generated from this contract and then implemented by the application's REST controllers.

## Overview

The flow is:

```text
openapi.yaml
     │
     │ OpenAPI Generator
     ▼
UsersApi.java
     │
     │ implemented by
     ▼
UserController
     │
     ▼
Application Services
     │
     ▼
Domain
     │
     ▼
Infrastructure
```

The important principle is:

> **The OpenAPI specification defines the API contract. The Java controller implements that contract.**

The controller should not independently redefine the API contract.

---

## OpenAPI Specification

The API specification is stored at:

```text
src/main/resources/openapi/openapi.yaml
```

The specification defines:

* available endpoints
* HTTP methods
* URL paths
* path parameters
* query parameters
* request bodies
* response bodies
* HTTP status codes
* API schemas
* validation constraints
* API documentation

For example:

```yaml
servers:
  - url: http://localhost:8080/api

paths:
  /users:
    get:
      operationId: getUsers

    post:
      operationId: createUser

  /users/{userId}:
    get:
      operationId: getUser

    put:
      operationId: updateUser

    delete:
      operationId: deleteUser
```

The `/api` prefix is defined by the server URL:

```yaml
servers:
  - url: http://localhost:8080/api
```

Therefore:

```text
/users
```

in the OpenAPI document represents:

```text
/api/users
```

in the actual application.

---

# OpenAPI Generator

The project uses OpenAPI Generator to generate Java API interfaces from `openapi.yaml`.

The Maven plugin reads:

```text
src/main/resources/openapi/openapi.yaml
```

and generates Java source code under:

```text
target/generated-sources/openapi
```

For example:

```text
openapi.yaml
    ↓
OpenAPI Generator
    ↓
UsersApi.java
```

Generated code must not be manually edited.

If the API contract changes, the OpenAPI YAML is changed first and the generated sources are regenerated.

---

# Generated API Interface

For the User API, OpenAPI Generator produces an interface such as:

```java
UsersApi
```

Conceptually, it contains methods corresponding to the operations defined in the OpenAPI specification:

```java
public interface UsersApi {

    ResponseEntity<UserPage> getUsers(...);

    ResponseEntity<UserResponse> registerUser(...);

    ResponseEntity<UserResponse> getUser(UUID userId);

    ResponseEntity<UserResponse> updateUser(
        UUID userId,
        UpdateUserRequest request
    );

    ResponseEntity<Void> deleteUser(UUID userId);
}
```

The exact generated code is controlled by the OpenAPI Generator configuration and should not be modified manually.

---

# Controller Implementation

The REST controller implements the generated API interface.

For example:

```java
@RestController
public class UserController implements UsersApi {
    // implementation
}
```

This gives us a clear relationship:

```text
OpenAPI contract
       │
       ▼
   UsersApi
       │
       ▼
 UserController
```

The controller is therefore responsible for implementing the behavior defined by the API contract.

The controller should remain thin. It should primarily:

1. Receive the HTTP request.
2. Delegate to the appropriate application service.
3. Return the application result.

Business rules should not be implemented in the controller.

---

# Why Use This Approach?

Without contract-first development, the API definition can become duplicated across several places:

```text
Controller annotations
Swagger annotations
DTOs
Documentation
Frontend assumptions
```

These definitions can drift apart over time.

With contract-first development, the OpenAPI specification becomes the central definition:

```text
                ┌───────────────┐
                │ openapi.yaml  │
                └───────┬───────┘
                        │
                 OpenAPI Generator
                        │
                        ▼
                 ┌─────────────┐
                 │  UsersApi   │
                 └──────┬──────┘
                        │
                   implements
                        │
                        ▼
                 ┌─────────────┐
                 │UserController│
                 └──────┬──────┘
                        │
                        ▼
                Application Layer
```

This means the API contract can be reviewed independently of the implementation.

---

# API Models vs Domain Models

The models defined in OpenAPI are API models.

For example:

```text
CreateUserRequest
UpdateUserRequest
UserResponse
UserPage
```

These represent the HTTP API.

They are **not** the same as domain objects.

The domain may contain:

```text
User
UserId
Username
EmailAddress
UserStatus
```

The separation is intentional:

```text
HTTP/API
   │
   │ API DTOs
   ▼
Controller
   │
   ▼
Application
   │
   │ Domain objects
   ▼
Domain
```

The API contract can therefore evolve independently from the internal domain model.

---

# Source of Truth

The following rule applies to the project:

> **Changes to the REST API are made in `openapi.yaml` first.**

For example, if we want to add a new endpoint:

```text
GET /api/users/{userId}/performances
```

we first add it to:

```text
openapi.yaml
```

Then regenerate the API interfaces.

We do not manually add the endpoint to the controller first.

The development flow is:

```text
1. Change openapi.yaml
        ↓
2. Run OpenAPI Generator
        ↓
3. Generated API changes
        ↓
4. Implement the changes in the controller
        ↓
5. Implement application/domain behavior
```

This keeps the API contract and implementation synchronized.

---

# Generated Code

Generated sources belong under:

```text
target/generated-sources/openapi
```

They are build artifacts and should not be manually edited.

The source of truth is:

```text
src/main/resources/openapi/openapi.yaml
```

The generated code can be recreated at any time by running the Maven generation task.

```bash
mvn clean generate-sources
```

Therefore, generated sources should not contain business logic.

Business logic belongs in the application's:

```text
application/
domain/
infrastructure/
```

layers.

---

# Summary

TromboneMaster follows a contract-first REST API architecture:

```text
                    ┌─────────────────┐
                    │  openapi.yaml   │
                    │  API contract   │
                    └────────┬────────┘
                             │
                             │ generate
                             ▼
                    ┌─────────────────┐
                    │    UsersApi     │
                    │ generated code  │
                    └────────┬────────┘
                             │
                             │ implements
                             ▼
                    ┌─────────────────┐
                    │ UserController  │
                    │ application     │
                    │ adapter         │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │ Application     │
                    │ Services        │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │ Domain          │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │ Infrastructure  │
                    └─────────────────┘
```

The key architectural rule is:

> **OpenAPI defines what the API is. The controller implements it. The application and domain define what the system does.**
