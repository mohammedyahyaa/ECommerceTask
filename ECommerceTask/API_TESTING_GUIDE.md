# Complete API Testing Guide

## Base URL
```
http://localhost:8080
```

---

## Table of Contents
1. [Authentication APIs](#authentication-apis) - Public endpoints
2. [User Management APIs](#user-management-apis) - Admin only
3. [Product Management APIs](#product-management-apis) - Admin CRUD, Users can view
4. [Order Management APIs](#order-management-apis) - Users can place orders

---

## Authentication APIs

### 1. Register User (Public)
**Endpoint:** `POST /api/auth/register`

**Request:**
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

**Available Roles:** `USER`, `PREMIUM_USER`, `ADMIN`

**Response (201):**
```json
{
  "id": 1,
  "username": "john_doe",
  "role": "USER"
}
```

---

### 2. Login (Public)
**Endpoint:** `POST /api/auth/login`

**Request:**
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

**Response (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "john_doe",
  "role": "USER"
}
```

**⚠️ Save the token for subsequent requests!**

---

## User Management APIs

**All endpoints require ADMIN role and Bearer token**

### 3. Create User (Admin Only)
**Endpoint:** `POST /api/users`

**Headers:**
```
Authorization: Bearer <admin_token>
Content-Type: application/json
```

**Request:**
```json
{
  "username": "new_user",
  "password": "password123",
  "role": "USER"
}
```

**cURL:**
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

**Response (201):**
```json
{
  "id": 2,
  "username": "new_user",
  "role": "USER"
}
```

---

### 4. Get All Users (Admin Only)
**Endpoint:** `GET /api/users`

**Headers:**
```
Authorization: Bearer <admin_token>
```

**cURL:**
```bash
curl -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer <admin_token>"
```

**Response (200):**
```json
[
  {
    "id": 1,
    "username": "admin",
    "role": "ADMIN"
  },
  {
    "id": 2,
    "username": "john_doe",
    "role": "USER"
  }
]
```

---

### 5. Get User by ID (Admin Only)
**Endpoint:** `GET /api/users/{id}`

**Headers:**
```
Authorization: Bearer <admin_token>
```

**cURL:**
```bash
curl -X GET http://localhost:8080/api/users/1 \
  -H "Authorization: Bearer <admin_token>"
```

**Response (200):**
```json
{
  "id": 1,
  "username": "admin",
  "role": "ADMIN"
}
```

---

### 6. Update User (Admin Only)
**Endpoint:** `PUT /api/users/{id}`

**Headers:**
```
Authorization: Bearer <admin_token>
Content-Type: application/json
```

**Request:**
```json
{
  "username": "updated_user",
  "password": "newpassword123",
  "role": "PREMIUM_USER"
}
```

**cURL:**
```bash
curl -X PUT http://localhost:8080/api/users/2 \
  -H "Authorization: Bearer <admin_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "updated_user",
    "password": "newpassword123",
    "role": "PREMIUM_USER"
  }'
```

**Response (200):**
```json
{
  "id": 2,
  "username": "updated_user",
  "role": "PREMIUM_USER"
}
```

---

### 7. Delete User (Admin Only)
**Endpoint:** `DELETE /api/users/{id}`

**Headers:**
```
Authorization: Bearer <admin_token>
```

**cURL:**
```bash
curl -X DELETE http://localhost:8080/api/users/2 \
  -H "Authorization: Bearer <admin_token>"
```

**Response (204 No Content):**
(No response body)

---

## Product Management APIs

### 8. Create Product (Admin Only)
**Endpoint:** `POST /api/products`

**Headers:**
```
Authorization: Bearer <admin_token>
Content-Type: application/json
```

**Request:**
```json
{
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 999.99,
  "quantity": 50
}
```

**cURL:**
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Authorization: Bearer <admin_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 999.99,
    "quantity": 50
  }'
```

**Response (201):**
```json
{
  "id": 1,
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 999.99,
  "quantity": 50,
  "createdAt": "2024-01-01T12:00:00",
  "updatedAt": "2024-01-01T12:00:00"
}
```

---

### 9. Get All Products (Authenticated Users)
**Endpoint:** `GET /api/products`

**Headers:**
```
Authorization: Bearer <user_token>
```

**Query Parameters (Optional):**
- `name` - Filter by product name (partial match)
- `minPrice` - Minimum price
- `maxPrice` - Maximum price
- `available` - true/false for stock availability

**cURL Examples:**

Get all products:
```bash
curl -X GET http://localhost:8080/api/products \
  -H "Authorization: Bearer <user_token>"
```

Search by name:
```bash
curl -X GET "http://localhost:8080/api/products?name=laptop" \
  -H "Authorization: Bearer <user_token>"
```

Filter by price range:
```bash
curl -X GET "http://localhost:8080/api/products?minPrice=100&maxPrice=500" \
  -H "Authorization: Bearer <user_token>"
```

Filter available products only:
```bash
curl -X GET "http://localhost:8080/api/products?available=true" \
  -H "Authorization: Bearer <user_token>"
```

Combined filters:
```bash
curl -X GET "http://localhost:8080/api/products?name=laptop&minPrice=500&maxPrice=1500&available=true" \
  -H "Authorization: Bearer <user_token>"
```

**Response (200):**
```json
[
  {
    "id": 1,
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 999.99,
    "quantity": 50,
    "createdAt": "2024-01-01T12:00:00",
    "updatedAt": "2024-01-01T12:00:00"
  },
  {
    "id": 2,
    "name": "Mouse",
    "description": "Wireless mouse",
    "price": 29.99,
    "quantity": 100,
    "createdAt": "2024-01-01T12:00:00",
    "updatedAt": "2024-01-01T12:00:00"
  }
]
```

---

### 10. Get Product by ID (Authenticated Users)
**Endpoint:** `GET /api/products/{id}`

**Headers:**
```
Authorization: Bearer <user_token>
```

**cURL:**
```bash
curl -X GET http://localhost:8080/api/products/1 \
  -H "Authorization: Bearer <user_token>"
```

**Response (200):**
```json
{
  "id": 1,
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 999.99,
  "quantity": 50,
  "createdAt": "2024-01-01T12:00:00",
  "updatedAt": "2024-01-01T12:00:00"
}
```

---

### 11. Update Product (Admin Only)
**Endpoint:** `PUT /api/products/{id}`

**Headers:**
```
Authorization: Bearer <admin_token>
Content-Type: application/json
```

**Request:**
```json
{
  "name": "Updated Laptop",
  "description": "Updated description",
  "price": 1099.99,
  "quantity": 75
}
```

**cURL:**
```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Authorization: Bearer <admin_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Updated Laptop",
    "description": "Updated description",
    "price": 1099.99,
    "quantity": 75
  }'
```

**Response (200):**
```json
{
  "id": 1,
  "name": "Updated Laptop",
  "description": "Updated description",
  "price": 1099.99,
  "quantity": 75,
  "createdAt": "2024-01-01T12:00:00",
  "updatedAt": "2024-01-01T12:05:00"
}
```

---

### 12. Delete Product (Admin Only - Soft Delete)
**Endpoint:** `DELETE /api/products/{id}`

**Headers:**
```
Authorization: Bearer <admin_token>
```

**cURL:**
```bash
curl -X DELETE http://localhost:8080/api/products/1 \
  -H "Authorization: Bearer <admin_token>"
```

**Response (204 No Content):**
(No response body - product is soft deleted)

---

## Order Management APIs

### 13. Create Order (USER, PREMIUM_USER, ADMIN)
**Endpoint:** `POST /api/orders`

**Headers:**
```
Authorization: Bearer <user_token>
Content-Type: application/json
```

**Request:**
```json
{
  "items": [
    {
      "productId": 1,
      "quantity": 2
    },
    {
      "productId": 2,
      "quantity": 1
    }
  ]
}
```

**cURL:**
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Authorization: Bearer <user_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "items": [
      {
        "productId": 1,
        "quantity": 2
      },
      {
        "productId": 2,
        "quantity": 1
      }
    ]
  }'
```

**Response (201):**
```json
{
  "id": 1,
  "userId": 2,
  "username": "john_doe",
  "orderTotal": 2029.97,
  "createdAt": "2024-01-01T12:00:00",
  "items": [
    {
      "id": 1,
      "productId": 1,
      "productName": "Laptop",
      "quantity": 2,
      "unitPrice": 999.99,
      "discountApplied": 0.00,
      "totalPrice": 1999.98
    },
    {
      "id": 2,
      "productId": 2,
      "productName": "Mouse",
      "quantity": 1,
      "unitPrice": 29.99,
      "discountApplied": 0.00,
      "totalPrice": 29.99
    }
  ]
}
```

**Note:** Discounts are automatically applied based on user role and order total.

---

### 14. Get My Orders (USER, PREMIUM_USER, ADMIN)
**Endpoint:** `GET /api/orders/my-orders`

**Headers:**
```
Authorization: Bearer <user_token>
```

**cURL:**
```bash
curl -X GET http://localhost:8080/api/orders/my-orders \
  -H "Authorization: Bearer <user_token>"
```

**Response (200):**
```json
[
  {
    "id": 1,
    "userId": 2,
    "username": "john_doe",
    "orderTotal": 2029.97,
    "createdAt": "2024-01-01T12:00:00",
    "items": [...]
  }
]
```

---

### 15. Get Order by ID (Owner or Admin)
**Endpoint:** `GET /api/orders/{id}`

**Headers:**
```
Authorization: Bearer <user_token>
```

**cURL:**
```bash
curl -X GET http://localhost:8080/api/orders/1 \
  -H "Authorization: Bearer <user_token>"
```

**Response (200):**
```json
{
  "id": 1,
  "userId": 2,
  "username": "john_doe",
  "orderTotal": 2029.97,
  "createdAt": "2024-01-01T12:00:00",
  "items": [
    {
      "id": 1,
      "productId": 1,
      "productName": "Laptop",
      "quantity": 2,
      "unitPrice": 999.99,
      "discountApplied": 0.00,
      "totalPrice": 1999.98
    }
  ]
}
```

---

### 16. Get All Orders (Admin Only)
**Endpoint:** `GET /api/orders`

**Headers:**
```
Authorization: Bearer <admin_token>
```

**cURL:**
```bash
curl -X GET http://localhost:8080/api/orders \
  -H "Authorization: Bearer <admin_token>"
```

**Response (200):**
```json
[
  {
    "id": 1,
    "userId": 2,
    "username": "john_doe",
    "orderTotal": 2029.97,
    "createdAt": "2024-01-01T12:00:00",
    "items": [...]
  },
  {
    "id": 2,
    "userId": 3,
    "username": "premium_user",
    "orderTotal": 899.91,
    "createdAt": "2024-01-01T12:30:00",
    "items": [...]
  }
]
```

---

## Complete Testing Workflow

### Step 1: Setup - Create Admin User
```bash
# Register admin user
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123",
    "role": "ADMIN"
  }'
```

### Step 2: Login as Admin
```bash
# Login and save token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

**Save the token from response (e.g., `ADMIN_TOKEN=eyJhbGciOiJIUzI1NiJ9...`)**

### Step 3: Create Products (as Admin)
```bash
# Create Product 1
curl -X POST http://localhost:8080/api/products \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 999.99,
    "quantity": 50
  }'

# Create Product 2
curl -X POST http://localhost:8080/api/products \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Mouse",
    "description": "Wireless mouse",
    "price": 29.99,
    "quantity": 100
  }'
```

### Step 4: Create Regular User
```bash
# Register regular user
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user1",
    "password": "password123",
    "role": "USER"
  }'
```

### Step 5: Login as Regular User
```bash
# Login and save token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user1",
    "password": "password123"
  }'
```

**Save the token (e.g., `USER_TOKEN=eyJhbGciOiJIUzI1NiJ9...`)**

### Step 6: View Products (as User)
```bash
# Get all products
curl -X GET http://localhost:8080/api/products \
  -H "Authorization: Bearer $USER_TOKEN"

# Get specific product
curl -X GET http://localhost:8080/api/products/1 \
  -H "Authorization: Bearer $USER_TOKEN"
```

### Step 7: Place Order (as User)
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Authorization: Bearer $USER_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "items": [
      {
        "productId": 1,
        "quantity": 2
      },
      {
        "productId": 2,
        "quantity": 1
      }
    ]
  }'
```

### Step 8: View My Orders (as User)
```bash
curl -X GET http://localhost:8080/api/orders/my-orders \
  -H "Authorization: Bearer $USER_TOKEN"
```

---

## Discount Examples

### Example 1: USER Role (No Discount)
**Order Total:** $500.00
**Discount Applied:** $0.00
**Final Total:** $500.00

### Example 2: PREMIUM_USER Role (10% Discount)
**Order Total:** $500.00
**Premium Discount (10%):** $50.00
**Final Total:** $450.00

### Example 3: Order > $500 (5% Extra Discount)
**Order Total:** $600.00
**High Order Discount (5%):** $30.00
**Final Total (USER):** $570.00
**Final Total (PREMIUM_USER):** $540.00 (10% + 5% = 15% total)

---

## Error Responses

### 401 Unauthorized
```json
{
  "timestamp": "2024-01-01T12:00:00",
  "status": 401,
  "error": "UNAUTHORIZED",
  "message": "Invalid username or password",
  "path": "/api/auth/login"
}
```

### 403 Forbidden
```json
{
  "timestamp": "2024-01-01T12:00:00",
  "status": 403,
  "error": "FORBIDDEN",
  "message": "Access Denied",
  "path": "/api/users"
}
```

### 404 Not Found
```json
{
  "timestamp": "2024-01-01T12:00:00",
  "status": 404,
  "error": "NOT_FOUND",
  "message": "Product not found",
  "path": "/api/products/999"
}
```

### 400 Bad Request (Insufficient Stock)
```json
{
  "timestamp": "2024-01-01T12:00:00",
  "status": 400,
  "error": "BAD_REQUEST",
  "message": "Insufficient stock for product: Laptop. Available: 5, Requested: 10",
  "path": "/api/orders"
}
```

---

## Role-Based Access Summary

| Endpoint | USER | PREMIUM_USER | ADMIN |
|----------|------|--------------|-------|
| POST /api/auth/register | ✅ | ✅ | ✅ |
| POST /api/auth/login | ✅ | ✅ | ✅ |
| GET /api/products | ✅ | ✅ | ✅ |
| GET /api/products/{id} | ✅ | ✅ | ✅ |
| POST /api/products | ❌ | ❌ | ✅ |
| PUT /api/products/{id} | ❌ | ❌ | ✅ |
| DELETE /api/products/{id} | ❌ | ❌ | ✅ |
| POST /api/orders | ✅ | ✅ | ✅ |
| GET /api/orders/my-orders | ✅ | ✅ | ✅ |
| GET /api/orders/{id} | ✅* | ✅* | ✅ |
| GET /api/orders | ❌ | ❌ | ✅ |
| GET /api/users | ❌ | ❌ | ✅ |
| POST /api/users | ❌ | ❌ | ✅ |
| PUT /api/users/{id} | ❌ | ❌ | ✅ |
| DELETE /api/users/{id} | ❌ | ❌ | ✅ |

*Only if user owns the order

---

## Notes

1. **Token Expiration:** JWT tokens expire after 24 hours
2. **Token Format:** Always use `Bearer <token>` in Authorization header
3. **Stock Validation:** Orders are rejected if insufficient stock
4. **Inventory Update:** Product inventory decreases automatically on successful order
5. **Discounts:** Applied automatically based on user role and order total
6. **Soft Delete:** Products are soft deleted (not physically removed)

