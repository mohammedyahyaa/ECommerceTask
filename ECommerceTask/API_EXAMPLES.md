# API Authentication Examples

## Base URL
```
http://localhost:8080/api/auth
```

---

## 1. Register (Create New User)

**Endpoint:** `POST /api/auth/register`

**Request Body:**
```json
{
  "username": "john_doe",
  "password": "password123",
  "role": "USER"
}
```

**cURL:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "password123",
    "role": "USER"
  }'
```

**Example for different roles:**
- **USER:**
```json
{
  "username": "user1",
  "password": "password123",
  "role": "USER"
}
```

- **PREMIUM_USER:**
```json
{
  "username": "premium_user1",
  "password": "password123",
  "role": "PREMIUM_USER"
}
```

- **ADMIN:**
```json
{
  "username": "admin",
  "password": "admin123",
  "role": "ADMIN"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "username": "john_doe",
  "role": "USER"
}
```

---

## 2. Login (Generate JWT Token)

**Endpoint:** `POST /api/auth/login`

**Request Body:**
```json
{
  "username": "john_doe",
  "password": "password123"
}
```

**cURL:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "password123"
  }'
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huX2RvZSIsInJvbGUiOiJVU0VSIiwiaWF0IjoxNzM1NzY4MDAwLCJleHAiOjE3MzU4NTQ0MDB9.example_signature",
  "username": "john_doe",
  "role": "USER"
}
```

---

## 3. Using the JWT Token

After login, use the token in the `Authorization` header for protected endpoints:

**Example: Get Products**
```bash
curl -X GET http://localhost:8080/api/products \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huX2RvZSIsInJvbGUiOiJVU0VSIiwiaWF0IjoxNzM1NzY4MDAwLCJleHAiOjE3MzU4NTQ0MDB9.example_signature"
```

---

## Complete Workflow Example

### Step 1: Register a User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123",
    "role": "ADMIN"
  }'
```

### Step 2: Login to Get Token
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "admin",
  "role": "ADMIN"
}
```

### Step 3: Use Token for Protected Endpoints
```bash
# Save token to variable (in PowerShell)
$token = "eyJhbGciOiJIUzI1NiJ9..."

# Use token in subsequent requests
curl -X GET http://localhost:8080/api/products \
  -H "Authorization: Bearer $token"
```

---

## Postman Collection Example

### Register Request
- **Method:** POST
- **URL:** `http://localhost:8080/api/auth/register`
- **Headers:**
  - `Content-Type: application/json`
- **Body (raw JSON):**
```json
{
  "username": "testuser",
  "password": "password123",
  "role": "USER"
}
```

### Login Request
- **Method:** POST
- **URL:** `http://localhost:8080/api/auth/login`
- **Headers:**
  - `Content-Type: application/json`
- **Body (raw JSON):**
```json
{
  "username": "testuser",
  "password": "password123"
}
```

### Authenticated Request (Get Products)
- **Method:** GET
- **URL:** `http://localhost:8080/api/products`
- **Headers:**
  - `Authorization: Bearer <token_from_login_response>`

---

## Available Roles

- `USER` - Regular user, can view products
- `PREMIUM_USER` - Premium user, can view products
- `ADMIN` - Administrator, full CRUD access on products and users

---

## Notes

1. **Token Expiration:** JWT tokens expire after 24 hours (86400000 ms) by default
2. **Password Encoding:** Passwords are automatically hashed using BCrypt
3. **Public Endpoints:** `/api/auth/**` endpoints are public (no authentication required)
4. **Protected Endpoints:** All other endpoints require a valid JWT token in the Authorization header

