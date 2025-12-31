# Authentication Sequence - Step by Step Guide

## Overview
This guide shows the exact sequence of API calls needed to generate a JWT token and use it for authenticated requests.

---

## Step-by-Step Sequence

### Step 1: Register a User (First Time Only)

If you don't have a user account yet, register one first.

**Request:**
```http
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123",
  "role": "ADMIN"
}
```

**cURL:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123",
    "role": "ADMIN"
  }'
```

**Response (201 Created):**
```json
{
  "id": 1,
  "username": "admin",
  "role": "ADMIN"
}
```

**Note:** This step is only needed once to create the user. After that, you can skip to Step 2.

---

### Step 2: Login to Generate JWT Token

Use the registered credentials to login and receive a JWT token.

**Request:**
```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

**cURL:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTczNTc2ODAwMCwiZXhwIjoxNzM1ODU0NDAwfQ.example_signature",
  "username": "admin",
  "role": "ADMIN"
}
```

**⚠️ Important:** Copy the `token` value from the response. You'll need it in Step 3.

---

### Step 3: Use the JWT Token for Authenticated Requests

Include the token in the `Authorization` header as `Bearer <token>` for all protected endpoints.

**Example: Get All Products**
```http
GET http://localhost:8080/api/products
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTczNTc2ODAwMCwiZXhwIjoxNzM1ODU0NDAwfQ.example_signature
```

**cURL:**
```bash
curl -X GET http://localhost:8080/api/products \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTczNTc2ODAwMCwiZXhwIjoxNzM1ODU0NDAwfQ.example_signature"
```

---

## Complete Sequence Diagram

```
┌─────────┐                          ┌──────────┐
│ Client  │                          │   API    │
└────┬────┘                          └────┬─────┘
     │                                    │
     │  1. POST /api/auth/register       │
     │     {username, password, role}    │
     │──────────────────────────────────>│
     │                                    │
     │  2. Response: User created        │
     │     {id, username, role}          │
     │<──────────────────────────────────│
     │                                    │
     │  3. POST /api/auth/login          │
     │     {username, password}          │
     │──────────────────────────────────>│
     │                                    │
     │  4. Response: JWT Token           │
     │     {token, username, role}       │
     │<──────────────────────────────────│
     │                                    │
     │  5. GET /api/products             │
     │     Authorization: Bearer <token> │
     │──────────────────────────────────>│
     │                                    │
     │  6. Response: Products list       │
     │<──────────────────────────────────│
```

---

## PowerShell Script Example

```powershell
# Step 1: Register User (one time only)
$registerBody = @{
    username = "admin"
    password = "admin123"
    role = "ADMIN"
} | ConvertTo-Json

$registerResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/register" `
    -Method Post `
    -ContentType "application/json" `
    -Body $registerBody

Write-Host "User registered: $($registerResponse.username)"

# Step 2: Login to get token
$loginBody = @{
    username = "admin"
    password = "admin123"
} | ConvertTo-Json

$loginResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" `
    -Method Post `
    -ContentType "application/json" `
    -Body $loginBody

$token = $loginResponse.token
Write-Host "Token received: $token"

# Step 3: Use token for authenticated request
$headers = @{
    Authorization = "Bearer $token"
}

$products = Invoke-RestMethod -Uri "http://localhost:8080/api/products" `
    -Method Get `
    -Headers $headers

Write-Host "Products retrieved: $($products.Count) items"
```

---

## Bash Script Example

```bash
#!/bin/bash

# Step 1: Register User (one time only)
echo "Step 1: Registering user..."
REGISTER_RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123",
    "role": "ADMIN"
  }')

echo "User registered: $REGISTER_RESPONSE"

# Step 2: Login to get token
echo "Step 2: Logging in..."
LOGIN_RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }')

TOKEN=$(echo $LOGIN_RESPONSE | grep -o '"token":"[^"]*' | cut -d'"' -f4)
echo "Token received: $TOKEN"

# Step 3: Use token for authenticated request
echo "Step 3: Fetching products..."
PRODUCTS=$(curl -s -X GET http://localhost:8080/api/products \
  -H "Authorization: Bearer $TOKEN")

echo "Products: $PRODUCTS"
```

---

## Quick Reference

### Endpoints Summary

| Step | Method | Endpoint | Auth Required | Purpose |
|------|--------|----------|---------------|---------|
| 1 | POST | `/api/auth/register` | ❌ No | Create new user account |
| 2 | POST | `/api/auth/login` | ❌ No | Get JWT token |
| 3 | GET/POST/PUT/DELETE | `/api/products/*` | ✅ Yes | Use token for protected endpoints |

### Token Usage

- **Header Name:** `Authorization`
- **Format:** `Bearer <your_jwt_token>`
- **Token Expiry:** 24 hours (86400000 ms)
- **Refresh:** Login again to get a new token

### Common Error Responses

**401 Unauthorized (Invalid Credentials):**
```json
{
  "timestamp": "2024-01-01T12:00:00",
  "status": 401,
  "error": "UNAUTHORIZED",
  "message": "Invalid username or password",
  "path": "/api/auth/login"
}
```

**400 Bad Request (Validation Error):**
```json
{
  "timestamp": "2024-01-01T12:00:00",
  "status": 400,
  "error": "VALIDATION_ERROR",
  "message": "username must not be blank, password must not be blank",
  "path": "/api/auth/login"
}
```

---

## Notes

1. **One-Time Registration:** Step 1 (register) only needs to be done once per user
2. **Token Storage:** Save the token from Step 2 for subsequent requests
3. **Token Refresh:** If token expires (24 hours), repeat Step 2 to get a new token
4. **Security:** Never share or expose your JWT token in public repositories or logs

