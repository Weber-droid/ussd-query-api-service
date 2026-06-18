# API Integration Test Guide

> Verified against live responses from `scripts/seed-test-data.sql` seed records.  
> Base URL: `http://localhost:8080`

---

## Prerequisites

1. PostgreSQL running with database `ussd` and table `call_detail_records` created (see [README.md](README.md#database-setup)).
2. Seed data loaded:

```bash
psql -d ussd -f scripts/seed-test-data.sql
```

3. Application running:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

---

## Seed Data Reference

The seed script loads **11 CDR records** across two time windows:

| Window | Records | Purpose |
|--------|---------|---------|
| `2023-08-18 10:00:00` – `10:01:00` | 10 | Production CDRs + IMSI supplement |
| `2023-08-18 10:30:00` – `10:31:00` | 1 | Specification example window |

### Supplementary rows (enable IMSI / combined filters)

| ID | RECORD_DATE | MSISDN | IMSI | Purpose |
|----|-------------|--------|------|---------|
| `1c3394ad…` *(updated)* | 2023-08-18 10:00:00.024 | 573228553366 | `732101647793504` | Combined MSISDN + IMSI filter |
| `d4e5f6a7-0001…` | 2023-08-18 10:00:00.500 | 573228550001 | `1234567890` | IMSI-only filter (10:00 window) |
| `d4e5f6a7-0002…` | 2023-08-18 10:30:15 | 573228550000 | `1234567890` | Spec examples 1–4 (10:30 window) |

**Note:** `RECORD_DATE` is returned without milliseconds (`yyyy-MM-dd HH:mm:ss`) per Jackson serialization config.

---

## Endpoint

| Property | Value |
|----------|-------|
| Method | `POST` |
| Path | `/api/v1/ussd/query` |
| Content-Type | `application/json` |

---

## Test 1 — Date Range Only

Matches **Example 1** from the specification (`10:30` window variant in [Test 5](#test-5--specification-date-window)).

### Request (10:00 window — all production + IMSI supplement)

```json
{
  "record_date_start": "2023-08-18 10:00:00",
  "record_date_end": "2023-08-18 10:01:00"
}
```

### cURL

```bash
curl -X POST http://localhost:8080/api/v1/ussd/query \
  -H "Content-Type: application/json" \
  -d '{
    "record_date_start": "2023-08-18 10:00:00",
    "record_date_end": "2023-08-18 10:01:00"
  }'
```

### Response — `200 OK`

```json
[
  { "IMSI": "732101647793504", "MSISDN": "573228553366", "RECORD_DATE": "2023-08-18 10:00:00" },
  { "IMSI": null, "MSISDN": "573113244726", "RECORD_DATE": "2023-08-18 10:00:00" },
  { "IMSI": null, "MSISDN": "573164454442", "RECORD_DATE": "2023-08-18 10:00:00" },
  { "IMSI": null, "MSISDN": "573138055627", "RECORD_DATE": "2023-08-18 10:00:00" },
  { "IMSI": null, "MSISDN": "573118221206", "RECORD_DATE": "2023-08-18 10:00:00" },
  { "IMSI": null, "MSISDN": "573107255337", "RECORD_DATE": "2023-08-18 10:00:00" },
  { "IMSI": null, "MSISDN": "573217607651", "RECORD_DATE": "2023-08-18 10:00:00" },
  { "IMSI": null, "MSISDN": "573235238047", "RECORD_DATE": "2023-08-18 10:00:00" },
  { "IMSI": null, "MSISDN": "573123466571", "RECORD_DATE": "2023-08-18 10:00:00" },
  { "IMSI": "1234567890", "MSISDN": "573228550001", "RECORD_DATE": "2023-08-18 10:00:00" }
]
```

**Record count:** 10

---

## Test 2 — Date Range + MSISDN

Matches **Example 2** from the specification.

### Request

```json
{
  "record_date_start": "2023-08-18 10:00:00",
  "record_date_end": "2023-08-18 10:01:00",
  "msisdn": "573228553366"
}
```

### cURL

```bash
curl -X POST http://localhost:8080/api/v1/ussd/query \
  -H "Content-Type: application/json" \
  -d '{
    "record_date_start": "2023-08-18 10:00:00",
    "record_date_end": "2023-08-18 10:01:00",
    "msisdn": "573228553366"
  }'
```

### Response — `200 OK`

```json
[
  {
    "IMSI": "732101647793504",
    "MSISDN": "573228553366",
    "RECORD_DATE": "2023-08-18 10:00:00"
  }
]
```

**Record count:** 1

### Specification example (`573228550000` in 10:30 window)

```json
{
  "record_date_start": "2023-08-18 10:30:00",
  "record_date_end": "2023-08-18 10:31:00",
  "msisdn": "573228550000"
}
```

### Response — `200 OK`

```json
[
  {
    "IMSI": "1234567890",
    "MSISDN": "573228550000",
    "RECORD_DATE": "2023-08-18 10:30:15"
  }
]
```

**Record count:** 1

---

## Test 3 — Date Range + IMSI

Matches **Example 3** from the specification.

### Request (specification IMSI value)

```json
{
  "record_date_start": "2023-08-18 10:00:00",
  "record_date_end": "2023-08-18 10:01:00",
  "imsi": "1234567890"
}
```

### cURL

```bash
curl -X POST http://localhost:8080/api/v1/ussd/query \
  -H "Content-Type: application/json" \
  -d '{
    "record_date_start": "2023-08-18 10:00:00",
    "record_date_end": "2023-08-18 10:01:00",
    "imsi": "1234567890"
  }'
```

### Response — `200 OK`

```json
[
  {
    "IMSI": "1234567890",
    "MSISDN": "573228550001",
    "RECORD_DATE": "2023-08-18 10:00:00"
  }
]
```

**Record count:** 1

### Alternate IMSI (`732101647793504`)

```json
{
  "record_date_start": "2023-08-18 10:00:00",
  "record_date_end": "2023-08-18 10:01:00",
  "imsi": "732101647793504"
}
```

### Response — `200 OK`

```json
[
  {
    "IMSI": "732101647793504",
    "MSISDN": "573228553366",
    "RECORD_DATE": "2023-08-18 10:00:00"
  }
]
```

**Record count:** 1

---

## Test 4 — Date Range + MSISDN + IMSI

Matches **Example 4** from the specification.

### Request (specification values — 10:30 window)

```json
{
  "record_date_start": "2023-08-18 10:30:00",
  "record_date_end": "2023-08-18 10:31:00",
  "msisdn": "573228550000",
  "imsi": "1234567890"
}
```

### cURL

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

### Response — `200 OK`

```json
[
  {
    "IMSI": "1234567890",
    "MSISDN": "573228550000",
    "RECORD_DATE": "2023-08-18 10:30:15"
  }
]
```

**Record count:** 1

### Alternate combined filter (10:00 window)

```json
{
  "record_date_start": "2023-08-18 10:00:00",
  "record_date_end": "2023-08-18 10:01:00",
  "msisdn": "573228553366",
  "imsi": "732101647793504"
}
```

### Response — `200 OK`

```json
[
  {
    "IMSI": "732101647793504",
    "MSISDN": "573228553366",
    "RECORD_DATE": "2023-08-18 10:00:00"
  }
]
```

**Record count:** 1

---

## Test 5 — Specification Date Window

Matches **Example 1** date range from the specification document (`10:30:00` – `10:31:00`).

### Request

```json
{
  "record_date_start": "2023-08-18 10:30:00",
  "record_date_end": "2023-08-18 10:31:00"
}
```

### cURL

```bash
curl -X POST http://localhost:8080/api/v1/ussd/query \
  -H "Content-Type: application/json" \
  -d '{
    "record_date_start": "2023-08-18 10:30:00",
    "record_date_end": "2023-08-18 10:31:00"
  }'
```

### Response — `200 OK`

```json
[
  {
    "IMSI": "1234567890",
    "MSISDN": "573228550000",
    "RECORD_DATE": "2023-08-18 10:30:15"
  }
]
```

**Record count:** 1

---

## Test 6 — Validation Error (Missing Required Field)

### Request

```json
{
  "record_date_end": "2023-08-18 10:01:00"
}
```

### cURL

```bash
curl -X POST http://localhost:8080/api/v1/ussd/query \
  -H "Content-Type: application/json" \
  -d '{
    "record_date_end": "2023-08-18 10:01:00"
  }'
```

### Response — `400 Bad Request`

```json
{
  "timestamp": "2026-06-18T09:58:32.535138591Z",
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

## Test Summary

| Test | Filters | HTTP Status | Records Returned |
|------|---------|-------------|------------------|
| 1 | Date range `10:00`–`10:01` | 200 | 10 |
| 2 | Date + MSISDN `573228553366` | 200 | 1 |
| 2b | Date + MSISDN `573228550000` (spec window) | 200 | 1 |
| 3 | Date + IMSI `1234567890` | 200 | 1 |
| 3b | Date + IMSI `732101647793504` | 200 | 1 |
| 4 | Date + MSISDN + IMSI (spec values, `10:30` window) | 200 | 1 |
| 4b | Date + MSISDN + IMSI (`573228553366` / `732101647793504`) | 200 | 1 |
| 5 | Spec date window `10:30`–`10:31` | 200 | 1 |
| 6 | Missing `record_date_start` | 400 | Error JSON |

---

## Postman Quick Setup

1. Create collection: **USSD Query API**
2. Add request: `POST {{base_url}}/api/v1/ussd/query`
3. Set environment variable: `base_url = http://localhost:8080`
4. Body type: **raw → JSON**
5. Paste any request payload from the tests above and compare against documented responses
