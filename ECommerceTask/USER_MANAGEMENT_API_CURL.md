# User Management APIs - cURL Commands

## Prerequisites

Before using these APIs, you need:
1. An admin user account
2. A JWT token from login

**Getting Admin Token:**
```bash
# First, register an admin (if not exists)
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123",
    "role": "ADMIN"
  }'

# Then login to get token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

**Save the token from login response and use it in all requests below.**
**Replace `<admin_token>` with your actual token.**

---

## 1. Create User (Admin Only)

**Endpoint:** `POST /api/users`

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Authorization: Bearer <admin_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "new_user",
    "password": "password123",
    "role": "USER"
  }'
```

**Example with different roles:**

```bash
# Create USER role
curl -X POST http://localhost:8080/api/users \
  -H "Authorization: Bearer <admin_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "regular_user",
    "password": "password123",
    "role": "USER"
  }'

# Create PREMIUM_USER role
curl -X POST http://localhost:8080/api/users \
  -H "Authorization: Bearer <admin_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "premium_user",
    "password": "password123",
    "role": "PREMIUM_USER"
  }'

# Create ADMIN role
curl -X POST http://localhost:8080/api/users \
  -H "Authorization: Bearer <admin_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin2",
    "password": "admin123",
    "role": "ADMIN"
  }'
```

**Expected Response (201 Created):**
```json
{
  "id": 2,
  "username": "new_user",
  "role": "USER"
}
```

---

## 2. Get All Users (Admin Only)

**Endpoint:** `GET /api/users`

```bash
curl -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer <admin_token>"
```

**Expected Response (200 OK):**
```json
[
  {
    "id": 1,
    "username": "admin",
    "role": "ADMIN"
  },
  {
    "id": 2,
    "username": "new_user",
    "role": "USER"
  },
  {
    "id": 3,
    "username": "premium_user",
    "role": "PREMIUM_USER"
  }
]
```

---

## 3. Get User by ID (Admin Only)

**Endpoint:** `GET /api/users/{id}`

```bash
# Get user with ID 1
curl -X GET http://localhost:8080/api/users/1 \
  -H "Authorization: Bearer <admin_token>"

# Get user with ID 2
curl -X GET http://localhost:8080/api/users/2 \
  -H "Authorization: Bearer <admin_token>"
```

**Expected Response (200 OK):**
```json
{
  "id": 1,
  "username": "admin",
  "role": "ADMIN"
}
```

---

## 4. Update User (Admin Only)

**Endpoint:** `PUT /api/users/{id}`

```bash
curl -X PUT http://localhost:8080/api/users/2 \
  -H "Authorization: Bearer <admin_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "updated_username",
    "password": "newpassword123",
    "role": "PREMIUM_USER"
  }'
```

**Example - Update username only (password and role remain same):**
```bash
curl -X PUT http://localhost:8080/api/users/2 \
  -H "Authorization: Bearer <admin_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "updated_username",
    "password": "password123",
    "role": "USER"
  }'
```

**Example - Upgrade user to PREMIUM_USER:**
```bash
curl -X PUT http://localhost:8080/api/users/2 \
  -H "Authorization: Bearer <admin_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "regular_user",
    "password": "password123",
    "role": "PREMIUM_USER"
  }'
```

**Expected Response (200 OK):**
```json
{
  "id": 2,
  "username": "updated_username",
  "role": "PREMIUM_USER"
}
```

---

## 5. Delete User (Admin Only)

**Endpoint:** `DELETE /api/users/{id}`

```bash
curl -X DELETE http://localhost:8080/api/users/2 \
  -H "Authorization: Bearer <admin_token>"
```

**Expected Response (204 No Content):**
*(No response body)*

---

## Complete Testing Workflow

### Step 1: Get Admin Token
```bash
# Login as admin
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

**Save the token from response (e.g., set as environment variable):**

**In PowerShell:**
```powershell
$ADMIN_TOKEN = "eyJhbGciOiJIUzI1NiJ9..."
```

**In Bash:**
```bash
export ADMIN_TOKEN="eyJhbGciOiJIUzI1NiJ9..."
```

### Step 2: Create Multiple Users
```bash
# Create USER
curl -X POST http://localhost:8080/api/users \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user1",
    "password": "password123",
    "role": "USER"
  }'

# Create PREMIUM_USER
curl -X POST http://localhost:8080/api/users \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "premium1",
    "password": "password123",
    "role": "PREMIUM_USER"
  }'
```

### Step 3: List All Users
```bash
curl -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer $ADMIN_TOKEN"
```

### Step 4: Get Specific User
```bash
# Get user with ID 2
curl -X GET http://localhost:8080/api/users/2 \
  -H "Authorization: Bearer $ADMIN_TOKEN"
```

### Step 5: Update User
```bash
# Upgrade user to premium
curl -X PUT http://localhost:8080/api/users/2 \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user1",
    "password": "password123",
    "role": "PREMIUM_USER"
  }'
```

### Step 6: Delete User (Optional)
```bash
curl -X DELETE http://localhost:8080/api/users/3 \
  -H "Authorization: Bearer $ADMIN_TOKEN"
```

---

## Error Responses

### 401 Unauthorized (Invalid/Missing Token)
```bash
curl -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer invalid_token"
```

**Response:**
```json
{
  "timestamp": "2024-01-01T12:00:00",
  "status": 401,
  "error": "UNAUTHORIZED",
  "message": "Full authentication is required to access this resource",
  "path": "/api/users"
}
```

### 403 Forbidden (Non-Admin User)
```bash
# Using a USER role token instead of ADMIN
curl -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer <user_token>"
```

**Response:**
```json
{
  "timestamp": "2024-01-01T12:00:00",
  "status": 403,
  "error": "FORBIDDEN",
  "message": "Access Denied",
  "path": "/api/users"
}
```

### 400 Bad Request (Username Already Exists)
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Authorization: Bearer <admin_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "password123",
    "role": "USER"
  }'
```

**Response:**
```json
{
  "timestamp": "2024-01-01T12:00:00",
  "status": 400,
  "error": "BAD_REQUEST",
  "message": "Username already exists",
  "path": "/api/users"
}
```

### 404 Not Found (User ID Not Found)
```bash
curl -X GET http://localhost:8080/api/users/999 \
  -H "Authorization: Bearer <admin_token>"
```

**Response:**
```json
{
  "timestamp": "2024-01-01T12:00:00",
  "status": 404,
  "error": "NOT_FOUND",
  "message": "User not found",
  "path": "/api/users/999"
}
```

### 400 Bad Request (Validation Error)
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Authorization: Bearer <admin_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "",
    "password": "",
    "role": "INVALID_ROLE"
  }'
```

**Response:**
```json
{
  "timestamp": "2024-01-01T12:00:00",
  "status": 400,
  "error": "VALIDATION_ERROR",
  "message": "username must not be blank, password must not be blank",
  "path": "/api/users"
}
```

---

## Quick Reference Table

| Method | Endpoint | Description | Requires Token |
|--------|----------|-------------|----------------|
| POST | `/api/users` | Create new user | ✅ Admin |
| GET | `/api/users` | Get all users | ✅ Admin |
| GET | `/api/users/{id}` | Get user by ID | ✅ Admin |
| PUT | `/api/users/{id}` | Update user | ✅ Admin |
| DELETE | `/api/users/{id}` | Delete user | ✅ Admin |

---

## Available Roles

- `USER` - Regular user
- `PREMIUM_USER` - Premium user (gets 10% discount on orders)
- `ADMIN` - Administrator (full access)

---

## Notes

1. **All endpoints require ADMIN role** - Only admin users can manage other users
2. **Token Required** - Always include `Authorization: Bearer <token>` header
3. **Password Encoding** - Passwords are automatically hashed using BCrypt
4. **Username Uniqueness** - Usernames must be unique across all users
5. **Validation** - All fields (username, password, role) are required and validated

