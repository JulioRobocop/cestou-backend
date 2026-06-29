<div align="center">

# 🔧 Cestou — Backend

**REST API for the Cestou internal marketplace**

<br/>

![Java](https://img.shields.io/badge/Java_17-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3.x-6DB33F?style=flat-square&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=flat-square&logo=springsecurity&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL_15-316192?style=flat-square&logo=postgresql&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate_ORM-59666C?style=flat-square&logo=hibernate&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-CC0200?style=flat-square&logo=flyway&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=flat-square&logo=jsonwebtokens&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger_UI-85EA2D?style=flat-square&logo=swagger&logoColor=black)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=flat-square&logo=apachemaven&logoColor=white)

[← Back to main README](./README.md)

</div>

---

## 📖 Table of Contents

- [Overview](#-overview)
- [Tech Stack In Depth](#-tech-stack-in-depth)
- [Project Structure](#-project-structure)
- [Layered Architecture](#-layered-architecture)
- [Security & JWT](#-security--jwt)
- [API Endpoints](#-api-endpoints)
- [Data Model & Entities](#-data-model--entities)
- [Database Migrations](#-database-migrations-flyway)
- [Error Handling](#-error-handling)
- [Getting Started](#-getting-started)
- [Environment Variables](#-environment-variables)
- [Running with Docker](#-running-with-docker)
- [Testing](#-testing)

---

## 🎯 Overview

The Cestou backend is a **stateless, JWT-secured REST API** built on Spring Boot 3. It is responsible for:

- Managing user accounts with role-based access control (USER / ADMIN)
- Exposing CRUD operations for voucher listings created by employees
- Processing and tracking purchase transactions between employees
- Providing administrative endpoints for platform oversight and reporting
- Enforcing business rules (e.g. an employee cannot buy their own listing, quantity cannot exceed available stock)

The API speaks JSON over HTTP and is consumed exclusively by the Vue 3 frontend. All sensitive operations require a valid JWT Bearer token in the `Authorization` header.

---

## 🛠️ Tech Stack In Depth

### Core Framework

**Java 21** is the language version. The project uses **Spring Boot 3.x** as the application framework, which brings together:

- `spring-boot-starter-web` — Embedded Tomcat, Jackson JSON serialization, `@RestController` support
- `spring-boot-starter-validation` — Bean Validation (JSR-380) via Hibernate Validator, used on all incoming DTOs
- `spring-boot-starter-actuator` — Health check and metrics endpoints (`/actuator/health`)

### Persistence

**Spring Data JPA** with **Hibernate** as the ORM handles all database communication. Entities are annotated with standard JPA annotations (`@Entity`, `@ManyToOne`, etc.) and repositories extend `JpaRepository<T, ID>`, providing zero-boilerplate CRUD and the ability to define custom JPQL queries with `@Query`.

**PostgreSQL 15** is the production database. **Flyway** manages schema evolution through versioned migration scripts in `src/main/resources/db/migration/`. Every structural change to the schema (table creation, new column, index) goes through a `V{n}__description.sql` migration, making the database reproducible from scratch.

### Security

**Spring Security 6** secures the application. A custom `JwtAuthenticationFilter` extends `OncePerRequestFilter` and intercepts every incoming request before it reaches any controller. It extracts and validates the JWT, then sets the `SecurityContextHolder` with the authenticated principal so that `@PreAuthorize` annotations work throughout the service layer.

Password hashing uses **BCrypt** with a cost factor of 10. JWTs are signed with **HMAC-SHA256 (HS256)** using a secret key stored in environment variables — never hardcoded.

### API Documentation

**SpringDoc OpenAPI 3** auto-generates a live Swagger UI at `/swagger-ui.html` from the controller annotations. All endpoints, request bodies, and response schemas are documented there without any extra manual work.

### Build & Packaging

**Maven** is the build tool. The `pom.xml` declares all dependencies. The final artifact is a self-contained fat JAR (`java -jar cestou-backend.jar`) runnable without an external application server. The `Dockerfile` uses a multi-stage build: the first stage compiles with a JDK image, the second stage runs on a slim JRE image, keeping the final Docker image lean.

---

## 📁 Project Structure

```
cestou-backend/
├── src/
│   ├── main/
│   │   ├── java/com/pado/cestou/
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java       # Spring Security bean, filter chain, CORS
│   │   │   │   ├── OpenApiConfig.java        # Swagger/OpenAPI configuration
│   │   │   │   └── CorsConfig.java           # Allowed origins and methods
│   │   │   │
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java       # /api/auth — register, login, refresh
│   │   │   │   ├── UserController.java       # /api/users — profile, admin user mgmt
│   │   │   │   ├── ListingController.java    # /api/listings — marketplace listings
│   │   │   │   ├── TransactionController.java# /api/transactions — purchases
│   │   │   │   └── AdminController.java      # /api/admin — dashboard, reports
│   │   │   │
│   │   │   ├── dto/
│   │   │   │   ├── request/                  # Inbound payloads (validated with @Valid)
│   │   │   │   │   ├── LoginRequest.java
│   │   │   │   │   ├── RegisterRequest.java
│   │   │   │   │   ├── ListingRequest.java
│   │   │   │   │   └── TransactionRequest.java
│   │   │   │   └── response/                 # Outbound payloads (no entity leakage)
│   │   │   │       ├── AuthResponse.java
│   │   │   │       ├── UserResponse.java
│   │   │   │       ├── ListingResponse.java
│   │   │   │       └── TransactionResponse.java
│   │   │   │
│   │   │   ├── exception/
│   │   │   │   ├── GlobalExceptionHandler.java  # @ControllerAdvice — unified error shape
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   ├── UnauthorizedException.java
│   │   │   │   └── BusinessException.java        # Domain rule violations
│   │   │   │
│   │   │   ├── model/
│   │   │   │   ├── User.java                 # @Entity — users table
│   │   │   │   ├── Listing.java              # @Entity — listings table
│   │   │   │   ├── Transaction.java          # @Entity — transactions table
│   │   │   │   └── enums/
│   │   │   │       ├── Role.java             # ROLE_USER, ROLE_ADMIN
│   │   │   │       ├── ListingStatus.java    # ACTIVE, SOLD_OUT, CANCELLED
│   │   │   │       └── TransactionStatus.java# PENDING, CONFIRMED, CANCELLED
│   │   │   │
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── ListingRepository.java
│   │   │   │   └── TransactionRepository.java
│   │   │   │
│   │   │   ├── security/
│   │   │   │   ├── JwtUtil.java              # generateToken(), validateToken(), extractClaims()
│   │   │   │   ├── JwtAuthenticationFilter.java # OncePerRequestFilter — validates every request
│   │   │   │   └── UserDetailsServiceImpl.java  # Loads UserDetails from DB for Spring Security
│   │   │   │
│   │   │   └── service/
│   │   │       ├── AuthService.java          # register, login, refresh logic
│   │   │       ├── UserService.java          # profile management, admin ops
│   │   │       ├── ListingService.java       # listing CRUD + business rules
│   │   │       └── TransactionService.java   # purchase logic, status transitions
│   │   │
│   │   └── resources/
│   │       ├── application.yml               # Config loaded from env vars
│   │       └── db/migration/
│   │           ├── V1__create_users.sql
│   │           ├── V2__create_listings.sql
│   │           ├── V3__create_transactions.sql
│   │           └── V4__add_indexes.sql
│   │
│   └── test/
│       └── java/com/pado/cestou/
│           ├── service/                      # Unit tests (Mockito)
│           └── controller/                   # Integration tests (@SpringBootTest)
│
├── Dockerfile
├── docker-compose.yml
├── .env.example
├── pom.xml
└── README-backend.md
```

---

## 🏗️ Layered Architecture

The application enforces a strict separation of concerns across four layers:

```
HTTP Request
     │
     ▼
┌────────────────────────────────────────┐
│         Security Filter Layer           │
│  JwtAuthenticationFilter               │
│  • Extracts Bearer token               │
│  • Validates signature + expiry        │
│  • Sets SecurityContext principal      │
└────────────────┬───────────────────────┘
                 │  (if valid token)
                 ▼
┌────────────────────────────────────────┐
│           Controller Layer             │
│  @RestController  @RequestMapping      │
│  • Receives + deserializes JSON        │
│  • Runs @Valid on request DTOs         │
│  • Delegates entirely to service       │
│  • Returns ResponseEntity<DTO>         │
└────────────────┬───────────────────────┘
                 │
                 ▼
┌────────────────────────────────────────┐
│            Service Layer               │
│  @Service  @Transactional              │
│  • All business logic lives here       │
│  • Throws domain exceptions            │
│  • Orchestrates between repositories   │
│  • Maps entities ↔ DTOs               │
└────────────────┬───────────────────────┘
                 │
                 ▼
┌────────────────────────────────────────┐
│          Repository Layer              │
│  JpaRepository<Entity, Long>           │
│  • Hibernate-backed DB access          │
│  • Custom @Query for complex queries   │
│  • No business logic allowed here      │
└────────────────┬───────────────────────┘
                 │
                 ▼
          PostgreSQL 15
```

**Key design decisions:**

- JPA entities are **never returned directly** from controllers — they are always mapped to response DTOs before serialization, preventing accidental data leakage and decoupling the API contract from the DB schema.
- All transactional operations (`@Transactional`) live in the service layer.
- Controllers are intentionally thin: validate input, call service, return response.

---

## 🔐 Security & JWT

### Authentication Flow

```
1. POST /api/auth/login  { email, password }
         │
         ▼
2. UserDetailsServiceImpl loads user from DB
   BCryptPasswordEncoder.matches(raw, hash)
         │
         ▼
3. JwtUtil.generateToken(userDetails)
   → signs HS256 JWT with SECRET_KEY
   → embeds: subject=email, roles, iat, exp
         │
         ▼
4. Response: { accessToken, refreshToken, expiresIn }
         │
         ▼
5. All subsequent requests:
   Authorization: Bearer <accessToken>
         │
         ▼
6. JwtAuthenticationFilter validates:
   • Signature (SECRET_KEY)
   • Expiry (exp claim)
   • User still active in DB
   Sets SecurityContextHolder
```

### Token Configuration

| Property | Value |
|---|---|
| Signing algorithm | HMAC-SHA256 (HS256) |
| Access token lifetime | 1 hour (`3600000 ms`) |
| Refresh token lifetime | 7 days (`604800000 ms`) |
| Password hashing | BCrypt, cost factor 10 |
| Token location | `Authorization: Bearer <token>` header |

### Role Hierarchy

| Role | Permissions |
|---|---|
| `ROLE_USER` | Browse listings, create/edit own listings, buy vouchers, view own transactions |
| `ROLE_ADMIN` | Everything USER can do + manage all users, view all transactions, update transaction statuses, access dashboard |

Roles are embedded in the JWT claims and also persisted in the `users` table. Endpoint-level authorization uses `@PreAuthorize("hasRole('ADMIN')")` annotations on service methods.

---

## 🔌 API Endpoints

### 🔑 `/api/auth` — Authentication

| Method | Endpoint | JWT | Description |
|--------|----------|:---:|---|
| `POST` | `/api/auth/register` | ❌ | Create a new user account. Returns the user profile. |
| `POST` | `/api/auth/login` | ❌ | Authenticate with email + password. Returns `accessToken` + `refreshToken`. |
| `POST` | `/api/auth/refresh` | ✅ | Exchange a valid refresh token for a new access token. |

**Register request body:**
```json
{
  "name": "Maria Silva",
  "email": "maria@pado.com.br",
  "password": "minhasenha123"
}
```

**Login response:**
```json
{
  "accessToken": "eyJhbGci...",
  "refreshToken": "eyJhbGci...",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

---

### 👤 `/api/users` — User Management

| Method | Endpoint | JWT | Role | Description |
|--------|----------|:---:|:---:|---|
| `GET` | `/api/users/me` | ✅ | USER | Get the current authenticated user's full profile. |
| `PUT` | `/api/users/me` | ✅ | USER | Update name or password of the current user. |
| `GET` | `/api/users` | ✅ | ADMIN | Paginated list of all users in the system. |
| `GET` | `/api/users/{id}` | ✅ | ADMIN | Fetch a specific user by their ID. |
| `DELETE` | `/api/users/{id}` | ✅ | ADMIN | Soft-delete (deactivate) a user account. Does not delete data. |

---

### 📦 `/api/listings` — Voucher Marketplace

| Method | Endpoint | JWT | Role | Description |
|--------|----------|:---:|:---:|---|
| `GET` | `/api/listings` | ✅ | USER | Browse all `ACTIVE` listings. Supports `?page`, `?size`, `?sort` query params. |
| `GET` | `/api/listings/{id}` | ✅ | USER | Get full details of a single listing. |
| `GET` | `/api/listings/my` | ✅ | USER | Get all listings created by the authenticated user (any status). |
| `POST` | `/api/listings` | ✅ | USER | Create a new voucher listing. Sets status to `ACTIVE` automatically. |
| `PUT` | `/api/listings/{id}` | ✅ | USER | Update price, quantity, or description (restricted to the listing's owner). |
| `DELETE` | `/api/listings/{id}` | ✅ | USER | Cancel a listing. ADMIN can cancel any listing; USERs only their own. |

**Create listing request body:**
```json
{
  "title": "Cesta Básica Sadia",
  "description": "Voucher válido até dezembro/2024",
  "price": 150.00,
  "quantityAvailable": 3
}
```

---

### 🛒 `/api/transactions` — Purchase Flow

| Method | Endpoint | JWT | Role | Description |
|--------|----------|:---:|:---:|---|
| `POST` | `/api/transactions` | ✅ | USER | Purchase a voucher. Validates stock availability and prevents self-purchase. |
| `GET` | `/api/transactions/my` | ✅ | USER | Get the current user's full purchase history. |
| `GET` | `/api/transactions/{id}` | ✅ | USER | Get a specific transaction (buyer or seller can access). |
| `GET` | `/api/transactions` | ✅ | ADMIN | Paginated list of all platform transactions. |
| `PUT` | `/api/transactions/{id}/status` | ✅ | ADMIN | Update transaction status to `CONFIRMED` or `CANCELLED`. |

**Purchase request body:**
```json
{
  "listingId": 42,
  "quantity": 1
}
```

---

### ⚙️ `/api/admin` — Administration

| Method | Endpoint | JWT | Role | Description |
|--------|----------|:---:|:---:|---|
| `GET` | `/api/admin/dashboard` | ✅ | ADMIN | Returns platform stats: total users, active listings, total revenue, pending transactions. |
| `GET` | `/api/admin/reports` | ✅ | ADMIN | Generates a transaction activity report filterable by date range. |

---

## 🗃️ Data Model & Entities

```
┌──────────────────────┐
│        users         │
├──────────────────────┤
│ id          BIGSERIAL│◄──────────────────────────────┐
│ name        VARCHAR  │                                │
│ email       VARCHAR  │  (unique)                      │
│ password    VARCHAR  │  (bcrypt hash)                 │
│ role        VARCHAR  │  ROLE_USER | ROLE_ADMIN        │
│ active      BOOLEAN  │  default true                  │
│ created_at  TIMESTAMP│                                │
│ updated_at  TIMESTAMP│                                │
└──────────────────────┘                                │
                                                        │
┌──────────────────────┐                                │
│       listings       │                                │
├──────────────────────┤                                │
│ id          BIGSERIAL│◄──────────────────┐            │
│ seller_id   BIGINT   │──── FK → users.id ┘────────────┘
│ title       VARCHAR  │                                │
│ description TEXT     │                                │
│ price       NUMERIC  │  (2 decimal places)            │
│ qty_avail   INTEGER  │                                │
│ status      VARCHAR  │  ACTIVE|SOLD_OUT|CANCELLED     │
│ created_at  TIMESTAMP│                                │
│ updated_at  TIMESTAMP│                                │
└──────────────────────┘                                │
         │                                              │
         │ FK                                           │
         ▼                                              │
┌──────────────────────┐                                │
│     transactions     │                                │
├──────────────────────┤                                │
│ id          BIGSERIAL│                                │
│ listing_id  BIGINT   │──── FK → listings.id           │
│ buyer_id    BIGINT   │──── FK → users.id ─────────────┘
│ quantity    INTEGER  │
│ total_price NUMERIC  │  (price × quantity at purchase time)
│ status      VARCHAR  │  PENDING|CONFIRMED|CANCELLED
│ created_at  TIMESTAMP│
│ updated_at  TIMESTAMP│
└──────────────────────┘
```

**Business rules enforced at the service layer:**

- A user cannot purchase their own listing (`listing.sellerId != buyer.id`)
- `quantity` requested cannot exceed `listing.quantityAvailable`
- When a transaction is `CONFIRMED`, `listing.quantityAvailable` is decremented; if it reaches 0, `listing.status` transitions to `SOLD_OUT`
- Only `ACTIVE` listings appear in the public browse endpoint
- Soft-deleted users (`active = false`) cannot authenticate

---

## 📦 Database Migrations (Flyway)

All schema changes are managed by **Flyway** and live in `src/main/resources/db/migration/`. Flyway runs automatically on startup and applies any pending migrations in order.

```
V1__create_users_table.sql         ← users table + unique index on email
V2__create_listings_table.sql      ← listings table + FK to users
V3__create_transactions_table.sql  ← transactions table + FKs
V4__add_performance_indexes.sql    ← indexes on seller_id, buyer_id, status columns
```

To add a new migration, create `V5__your_change.sql` — Flyway picks it up automatically on the next startup.

---

## ⚠️ Error Handling

All errors are returned in a consistent JSON envelope via `@ControllerAdvice`:

```json
{
  "timestamp": "2024-11-20T14:32:11.874Z",
  "status": 404,
  "error": "Not Found",
  "message": "Listing with id 99 was not found",
  "path": "/api/listings/99"
}
```

| Exception | HTTP Status |
|---|---|
| `ResourceNotFoundException` | `404 Not Found` |
| `UnauthorizedException` | `401 Unauthorized` |
| `AccessDeniedException` (Spring) | `403 Forbidden` |
| `BusinessException` | `422 Unprocessable Entity` |
| `MethodArgumentNotValidException` | `400 Bad Request` (includes field-level messages) |
| Unexpected `Exception` | `500 Internal Server Error` |

Validation errors on request bodies return a detailed list:
```json
{
  "status": 400,
  "error": "Validation Failed",
  "fields": {
    "email": "must be a valid email address",
    "price": "must be greater than 0"
  }
}
```

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- PostgreSQL 15+ (or Docker)

### Running Locally

```bash
# Clone
git clone https://github.com/JulioRobocop/cestou-backend.git
cd cestou-backend

# Set up environment
cp .env.example .env
# Edit .env with your local database credentials and JWT secret

# Build
./mvnw clean package -DskipTests

# Run
./mvnw spring-boot:run
```

The API starts at `http://localhost:8080`.  
Swagger UI: `http://localhost:8080/swagger-ui.html`

---

## 🔧 Environment Variables

```env
# ── Database ─────────────────────────────────────
DB_HOST=localhost
DB_PORT=5432
DB_NAME=cestou_db
DB_USERNAME=cestou_user
DB_PASSWORD=your_db_password

# ── JWT ──────────────────────────────────────────
JWT_SECRET=your-minimum-32-character-secret-key-here
JWT_EXPIRATION_MS=3600000        # 1 hour
JWT_REFRESH_EXPIRATION_MS=604800000  # 7 days

# ── Server ───────────────────────────────────────
SERVER_PORT=8080
CORS_ALLOWED_ORIGINS=http://localhost:5173

# ── Flyway ───────────────────────────────────────
FLYWAY_BASELINE_ON_MIGRATE=true
```

---

## 🐳 Running with Docker

```bash
# Start API + PostgreSQL together
docker compose up --build

# Detached mode
docker compose up -d

# View logs
docker compose logs -f cestou-api

# Tear down (keeps DB volume)
docker compose down

# Tear down and wipe DB
docker compose down -v
```

The `docker-compose.yml` defines two services:

| Service | Image | Port | Description |
|---|---|---|---|
| `postgres` | `postgres:15-alpine` | `5432` | Database with volume persistence |
| `cestou-api` | Built from `Dockerfile` | `8080` | Spring Boot application |

The API service depends on `postgres` and uses a health check to wait for it to be ready before starting.

---

## 🧪 Testing

```bash
# Run all tests
./mvnw test

# Run only unit tests
./mvnw test -Dtest="*ServiceTest"

# Run only integration tests
./mvnw test -Dtest="*ControllerTest"

# Generate coverage report (target/site/jacoco/index.html)
./mvnw verify
```

The test suite uses:
- **JUnit 5** for test structure
- **Mockito** for service-layer unit tests (repositories are mocked)
- **@SpringBootTest + MockMvc** for controller integration tests
- **H2 in-memory database** for integration tests (no real PostgreSQL needed)

---

<div align="center">

[← Back to main README](./README.md) · [→ Frontend README](./README-frontend.md)

Built with ☕ by **Julio César Gonçalves** — Pado S/A

</div>
