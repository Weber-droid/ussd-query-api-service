# Application 2: USSD Query API Service

> **High-performance Spring Boot REST API for querying telecom Call Detail Records (CDRs) from PostgreSQL.**

[![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.0-brightgreen?style=flat-square&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16+-blue?style=flat-square&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Maven](https://img.shields.io/badge/Maven-3.9+-red?style=flat-square&logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![CI/CD](https://img.shields.io/badge/CI%2FCD-GitHub%20Actions-2088FF?style=flat-square&logo=githubactions&logoColor=white)](.github/workflows/ci-cd.yml)
[![API Tests](https://img.shields.io/badge/API%20Tests-TEST.md-blue?style=flat-square)](TEST.md)

---

## API Testing

**Full integration test guide with verified request/response pairs:** [**TEST.md**](TEST.md)

Covers all four filter permutations (date only, date+MSISDN, date+IMSI, date+MSISDN+IMSI), empty-result cases, and validation errors — mapped to real data from `scripts/seed-test-data.sql`.

---

## Project Overview

This repository contains Application 2 (Query API Service), a core component of the USSD Event Processing infrastructure. The application is built using Spring Boot and Spring Data JPA to provide a high-performance RESTful API for querying telecom Call Detail Records (CDRs) persisted in a PostgreSQL database.

---

## Architectural Design & Optimizations

* **Dynamic Predicate Construction:** Built using the JPA Criteria API (Specifications) to handle optional query parameters seamlessly. The engine dynamically processes all four valid filtering permutations outlined in the specification without empty criteria collisions or query malformations.
* **Database Query Performance:** Designed for scale by utilizing a composite database index on (record_date, msisdn, imsi) to significantly minimize query execution times during high-volume telecom range scans.
* **Global Error Interception:** Implements a centralized Exception Handler (@ControllerAdvice) to capture validation and payload parsing errors, ensuring the API consistently returns clean, structured JSON error bodies instead of raw stack traces.

---

## Tech Stack & Prerequisites

### Core Stack

| Technology | Version / Role |
|---|---|
| **Java** | 17 (LTS) |
| **Spring Boot** | 4.1.0 |
| **Spring Web MVC** | REST API layer |
| **Spring Data JPA** | Persistence & dynamic query specifications |
| **Hibernate** | ORM (`ddl-auto: validate`) |
| **PostgreSQL** | Primary datastore (`ussd` database) |
| **Lombok** | Boilerplate reduction on entities/DTOs |
| **Maven** | Build, test, and packaging |
| **JUnit 5 + MockMvc** | Controller-level integration tests |
| **Checkstyle & SpotBugs** | Static analysis quality gates |

### Prerequisites

Before running locally, ensure the following are installed and available:

- **JDK 17+**
- **Maven 3.9+** (or use the included `./mvnw` wrapper)
- **PostgreSQL 14+** with a database named `ussd`
- Network access to your PostgreSQL instance on port `5432`

---

## Database Setup

### 1. Create the Database

```sql
CREATE DATABASE ussd;
```

### 2. Create the `call_detail_records` Table

```sql
CREATE TABLE call_detail_records (
    "ID"                  VARCHAR(150)  NOT NULL,
    "RECORD_DATE"         TIMESTAMP     NOT NULL,
    "L_SPC"               INTEGER,
    "L_SSN"               INTEGER,
    "L_RI"                INTEGER,
    "L_GT_I"              INTEGER,
    "L_GT_DIGITS"         VARCHAR(18),
    "R_SPC"               INTEGER,
    "R_SSN"               INTEGER,
    "R_RI"                INTEGER,
    "R_GT_I"              INTEGER,
    "R_GT_DIGITS"         VARCHAR(18),
    "SERVICE_CODE"        VARCHAR(50),
    "OR_NATURE"           INTEGER,
    "OR_PLAN"             INTEGER,
    "OR_DIGITS"           VARCHAR(18),
    "DE_NATURE"           INTEGER,
    "DE_PLAN"             INTEGER,
    "DE_DIGITS"           VARCHAR(18),
    "ISDN_NATURE"         INTEGER,
    "ISDN_PLAN"           INTEGER,
    "MSISDN"              VARCHAR(18),
    "VLR_NATURE"          INTEGER,
    "VLR_PLAN"            INTEGER,
    "VLR_DIGITS"          VARCHAR(18),
    "IMSI"                VARCHAR(100),
    "STATUS"              VARCHAR(30)   NOT NULL,
    "TYPE"                VARCHAR(30)   NOT NULL,
    "TSTAMP"              TIMESTAMP     NOT NULL,
    "LOCAL_DIALOG_ID"     BIGINT,
    "REMOTE_DIALOG_ID"    BIGINT,
    "DIALOG_DURATION"     BIGINT,
    "USSD_STRING"         VARCHAR(255),
    CONSTRAINT pk_call_detail_records PRIMARY KEY ("ID")
);
```

### 3. Create Performance Indexes

```sql
-- Composite index aligned to all query permutations
CREATE INDEX idx_cdr_record_date_msisdn_imsi
    ON call_detail_records ("RECORD_DATE", "MSISDN", "IMSI");

-- Supplementary indexes for partial-filter query plans
CREATE INDEX idx_cdr_record_date
    ON call_detail_records ("RECORD_DATE");

CREATE INDEX idx_cdr_msisdn
    ON call_detail_records ("MSISDN");

CREATE INDEX idx_cdr_imsi
    ON call_detail_records ("IMSI");
```

---

## Getting Started

### 1. Clone & Configure

```bash
git clone https://github.com/Weber-droid/ussd-query-api-service.git
cd ussd-query-api-service
git checkout feature/ussd-query-logic
```

Create a local `.env` from the example (never commit `.env`):

```bash
cp .env.example .env
```

Edit `.env` if your database host, name, or credentials differ. The same file is used by `docker-compose.yml` and the Spring app.

> Optional: create a local `application-local.yml` (gitignored) to override other settings for development.

### 2. Build the Application

```bash
./mvnw clean package
```

To run the full quality gate suite (Checkstyle, tests, SpotBugs):

```bash
./mvnw clean verify
```

### 3. Run the Application

**Option A — Docker Compose (Postgres + API together):**

```bash
cp .env.example .env   # first time only
docker compose up -d --build
```

Starts PostgreSQL and the API. Schema and seed data load automatically on **first** database init (empty volume). API: **`http://localhost:8080`**.

> If port `5432` is already in use on the host, set `DB_PORT=5433` in `.env` before `docker compose up`.

**Option B — Spring Boot Maven plugin (API on host, DB separate):**

```bash
./mvnw spring-boot:run
```

Use when developing with hot reload. Requires Postgres running (`docker compose up -d postgres` or local install) and manual schema/seed (see [Database Setup](#database-setup)).

**Option C — Executable JAR (production-like):**

```bash
java -jar target/ussd-query-api-service-0.0.1-SNAPSHOT.jar
```

The service starts on **`http://localhost:8080`** by default.

### 4. Verify Health

See **[TEST.md](TEST.md)** for complete request/response examples against seed data.

Quick smoke test:

```bash
curl -s -o /dev/null -w "%{http_code}" \
  -X POST http://localhost:8080/api/v1/ussd/query \
  -H "Content-Type: application/json" \
  -d '{
    "record_date_start": "2023-08-18 10:00:00",
    "record_date_end": "2023-08-18 10:01:00"
  }'
```

A `200` response confirms the API is reachable and the database connection is healthy.

---

## API Endpoints & cURL Examples

### Query Call Detail Records

| Property | Value |
|---|---|
| **Method** | `POST` |
| **Path** | `/api/v1/ussd/query` |
| **Content-Type** | `application/json` |
| **Auth** | None (configure per deployment) |

#### Request Parameters

| Field | Required | Format | Description |
|---|---|---|---|
| `record_date_start` | Yes | `yyyy-MM-dd HH:mm:ss` | Start of the query window (inclusive) |
| `record_date_end` | Yes | `yyyy-MM-dd HH:mm:ss` | End of the query window (inclusive) |
| `msisdn` | No | String (max 18) | Filter by subscriber number |
| `imsi` | No | String (max 100) | Filter by IMSI identifier |

#### Response

Returns a **JSON array** of matching records. Each object contains **only**:

```json
{
  "RECORD_DATE": "2023-08-18 10:30:00",
  "MSISDN": "573228550000",
  "IMSI": "1234567890"
}
```

All other CDR fields are withheld from the response via `@JsonIgnore` on the entity layer.

---

### Example 1 — Date Range Only

```bash
curl -X POST http://localhost:8080/api/v1/ussd/query \
  -H "Content-Type: application/json" \
  -d '{
    "record_date_start": "2023-08-18 10:30:00",
    "record_date_end": "2023-08-18 10:31:00"
  }'
```

```json
{
  "record_date_start": "2023-08-18 10:30:00",
  "record_date_end": "2023-08-18 10:31:00"
}
```

---

### Example 2 — Date Range + MSISDN

```bash
curl -X POST http://localhost:8080/api/v1/ussd/query \
  -H "Content-Type: application/json" \
  -d '{
    "record_date_start": "2023-08-18 10:30:00",
    "record_date_end": "2023-08-18 10:31:00",
    "msisdn": "573228550000"
  }'
```

```json
{
  "record_date_start": "2023-08-18 10:30:00",
  "record_date_end": "2023-08-18 10:31:00",
  "msisdn": "573228550000"
}
```

---

### Example 3 — Date Range + IMSI

```bash
curl -X POST http://localhost:8080/api/v1/ussd/query \
  -H "Content-Type: application/json" \
  -d '{
    "record_date_start": "2023-08-18 10:30:00",
    "record_date_end": "2023-08-18 10:31:00",
    "imsi": "1234567890"
  }'
```

```json
{
  "record_date_start": "2023-08-18 10:30:00",
  "record_date_end": "2023-08-18 10:31:00",
  "imsi": "1234567890"
}
```

---

### Example 4 — Date Range + MSISDN + IMSI

```bash
curl -X POST http://localhost:8080/api/v1/ussd/query \
  -H "Content-Type: application/json" \
  -d '{
    "record_date_start": "2023-08-18 10:30:00",
    "record_date_end": "2023-08-18 10:31:00",
    "msisdn": "573228550000",
    "imsi": "1234567890"
  }'
```

```json
{
  "record_date_start": "2023-08-18 10:30:00",
  "record_date_end": "2023-08-18 10:31:00",
  "msisdn": "573228550000",
  "imsi": "1234567890"
}
```

---

### Error Response Format

When validation fails, the API returns `400 Bad Request` with a structured payload:

```json
{
  "timestamp": "2026-06-17T10:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/v1/ussd/query",
  "fieldErrors": [
    {
      "field": "recordDateStart",
      "message": "record_date_start is required"
    }
  ]
}
```

---

## Project Structure

```text
src/main/java/com/paicore/ussd_query_api_service/
├── controller/          # REST endpoint layer
├── dto/                 # Request contracts
├── entity/              # JPA entity + Jackson serialization rules
├── exception/           # GlobalExceptionHandler + ErrorResponse
├── repository/          # Spring Data JPA + Specification executor
└── service/             # Dynamic query composition
```

---

## License

Proprietary — PaiCore USSD Event Processing Infrastructure.
