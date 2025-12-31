# Order Management APIs - cURL Commands

## Prerequisites

Before using these APIs, you need:
1. A user account (USER, PREMIUM_USER, or ADMIN)
2. Products created in the system
3. A JWT token from login

**Getting User Token:**
```bash
# Register a user (if not exists)
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user1",
    "password": "password123",
    "role": "USER"
  }'

# Then login to get token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user1",
    "password": "password123"
  }'
```

**Save the token from login response and use it in all requests below.**
**Replace `<user_token>` with your actual token.**

---

## 1. Create Order (USER, PREMIUM_USER, ADMIN)

**Endpoint:** `POST /api/orders`

**Headers:**
```
Authorization: Bearer <user_token>
Content-Type: application/json
```

### Example 1: Single Product Order
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Authorization: Bearer <user_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "items": [
      {
        "productId": 1,
        "quantity": 2
      }
    ]
  }'
```

### Example 2: Multiple Products Order
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
      },
      {
        "productId": 3,
        "quantity": 5
      }
    ]
  }'
```

### Example 3: Large Order (>$500 for extra discount)
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Authorization: Bearer <user_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "items": [
      {
        "productId": 1,
        "quantity": 3
      }
    ]
  }'
```

**Expected Response (201 Created):**
```json
{
  "id": 1,
  "userId": 2,
  "username": "user1",
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

---

## 2. Get My Orders (USER, PREMIUM_USER, ADMIN)

**Endpoint:** `GET /api/orders/my-orders`

**Headers:**
```
Authorization: Bearer <user_token>
```

```bash
curl -X GET http://localhost:8080/api/orders/my-orders \
  -H "Authorization: Bearer <user_token>"
```

**Expected Response (200 OK):**
```json
[
  {
    "id": 1,
    "userId": 2,
    "username": "user1",
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
  },
  {
    "id": 2,
    "userId": 2,
    "username": "user1",
    "orderTotal": 899.91,
    "createdAt": "2024-01-01T13:00:00",
    "items": [...]
  }
]
```

---

## 3. Get Order by ID (Owner or Admin)

**Endpoint:** `GET /api/orders/{id}`

**Headers:**
```
Authorization: Bearer <user_token>
```

```bash
# Get order with ID 1
curl -X GET http://localhost:8080/api/orders/1 \
  -H "Authorization: Bearer <user_token>"

# Get order with ID 2
curl -X GET http://localhost:8080/api/orders/2 \
  -H "Authorization: Bearer <user_token>"
```

**Expected Response (200 OK):**
```json
{
  "id": 1,
  "userId": 2,
  "username": "user1",
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

---

## 4. Get All Orders (Admin Only)

**Endpoint:** `GET /api/orders`

**Headers:**
```
Authorization: Bearer <admin_token>
```

```bash
curl -X GET http://localhost:8080/api/orders \
  -H "Authorization: Bearer <admin_token>"
```

**Expected Response (200 OK):**
```json
[
  {
    "id": 1,
    "userId": 2,
    "username": "user1",
    "orderTotal": 2029.97,
    "createdAt": "2024-01-01T12:00:00",
    "items": [...]
  },
  {
    "id": 2,
    "userId": 3,
    "username": "premium_user",
    "orderTotal": 899.91,
    "createdAt": "2024-01-01T13:00:00",
    "items": [...]
  },
  {
    "id": 3,
    "userId": 4,
    "username": "user2",
    "orderTotal": 549.99,
    "createdAt": "2024-01-01T14:00:00",
    "items": [...]
  }
]
```

---

## Complete Testing Workflow

### Step 1: Setup - Create Products (as Admin)
```bash
# Login as admin first
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'

# Save admin token (e.g., ADMIN_TOKEN="...")

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

# Create Product 3
curl -X POST http://localhost:8080/api/products \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Keyboard",
    "description": "Mechanical keyboard",
    "price": 149.99,
    "quantity": 75
  }'
```

### Step 2: Login as Regular User
```bash
# Login as user
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user1",
    "password": "password123"
  }'

# Save user token (e.g., USER_TOKEN="...")
```

### Step 3: View Available Products
```bash
# Get all products
curl -X GET http://localhost:8080/api/products \
  -H "Authorization: Bearer $USER_TOKEN"

# Get specific product
curl -X GET http://localhost:8080/api/products/1 \
  -H "Authorization: Bearer $USER_TOKEN"
```

### Step 4: Create Order
```bash
# Create order with multiple items
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

### Step 5: View My Orders
```bash
curl -X GET http://localhost:8080/api/orders/my-orders \
  -H "Authorization: Bearer $USER_TOKEN"
```

### Step 6: Get Specific Order
```bash
# Get order by ID (use ID from Step 4 response)
curl -X GET http://localhost:8080/api/orders/1 \
  -H "Authorization: Bearer $USER_TOKEN"
```

### Step 7: View All Orders (Admin Only)
```bash
# Login as admin and get token
curl -X GET http://localhost:8080/api/orders \
  -H "Authorization: Bearer $ADMIN_TOKEN"
```

---

## Discount Examples

### Example 1: USER Role Order (< $500)
**Request:**
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Authorization: Bearer <user_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "items": [
      {
        "productId": 2,
        "quantity": 10
      }
    ]
  }'
```
**Calculation:**
- Subtotal: $29.99 × 10 = $299.90
- USER Discount: $0.00
- High Order Discount (>$500): $0.00 (not applicable)
- **Order Total: $299.90**

### Example 2: PREMIUM_USER Role Order (< $500)
**Request:**
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Authorization: Bearer <premium_user_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "items": [
      {
        "productId": 2,
        "quantity": 10
      }
    ]
  }'
```
**Calculation:**
- Subtotal: $29.99 × 10 = $299.90
- Premium Discount (10%): $29.99
- High Order Discount (>$500): $0.00 (not applicable)
- **Order Total: $269.91**

### Example 3: USER Role Order (> $500)
**Request:**
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Authorization: Bearer <user_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "items": [
      {
        "productId": 1,
        "quantity": 1
      }
    ]
  }'
```
**Calculation:**
- Subtotal: $999.99 × 1 = $999.99
- USER Discount: $0.00
- High Order Discount (5%): $50.00
- **Order Total: $949.99**

### Example 4: PREMIUM_USER Role Order (> $500) - Both Discounts
**Request:**
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Authorization: Bearer <premium_user_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "items": [
      {
        "productId": 1,
        "quantity": 1
      }
    ]
  }'
```
**Calculation:**
- Subtotal: $999.99 × 1 = $999.99
- Premium Discount (10%): $100.00
- High Order Discount (5%): $50.00
- Total Discount: $150.00
- **Order Total: $849.99**

---

## Error Responses

### 401 Unauthorized (Invalid/Missing Token)
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Authorization: Bearer invalid_token" \
  -H "Content-Type: application/json" \
  -d '{
    "items": [
      {
        "productId": 1,
        "quantity": 2
      }
    ]
  }'
```

**Response:**
```json
{
  "timestamp": "2024-01-01T12:00:00",
  "status": 401,
  "error": "UNAUTHORIZED",
  "message": "Full authentication is required to access this resource",
  "path": "/api/orders"
}
```

### 403 Forbidden (Non-Authenticated User)
```bash
curl -X GET http://localhost:8080/api/orders/my-orders
```

**Response:**
```json
{
  "timestamp": "2024-01-01T12:00:00",
  "status": 403,
  "error": "FORBIDDEN",
  "message": "Access Denied",
  "path": "/api/orders/my-orders"
}
```

### 400 Bad Request (Insufficient Stock)
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Authorization: Bearer <user_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "items": [
      {
        "productId": 1,
        "quantity": 1000
      }
    ]
  }'
```

**Response:**
```json
{
  "timestamp": "2024-01-01T12:00:00",
  "status": 400,
  "error": "BAD_REQUEST",
  "message": "Insufficient stock for product: Laptop. Available: 50, Requested: 1000",
  "path": "/api/orders"
}
```

### 404 Not Found (Product Not Found)
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Authorization: Bearer <user_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "items": [
      {
        "productId": 999,
        "quantity": 1
      }
    ]
  }'
```

**Response:**
```json
{
  "timestamp": "2024-01-01T12:00:00",
  "status": 404,
  "error": "NOT_FOUND",
  "message": "Product not found with id: 999",
  "path": "/api/orders"
}
```

### 404 Not Found (Order Not Found)
```bash
curl -X GET http://localhost:8080/api/orders/999 \
  -H "Authorization: Bearer <user_token>"
```

**Response:**
```json
{
  "timestamp": "2024-01-01T12:00:00",
  "status": 404,
  "error": "NOT_FOUND",
  "message": "Order not found",
  "path": "/api/orders/999"
}
```

### 400 Bad Request (Access Denied - Viewing Other User's Order)
```bash
# User1 tries to view User2's order
curl -X GET http://localhost:8080/api/orders/2 \
  -H "Authorization: Bearer <user1_token>"
```

**Response:**
```json
{
  "timestamp": "2024-01-01T12:00:00",
  "status": 400,
  "error": "BAD_REQUEST",
  "message": "You don't have permission to view this order",
  "path": "/api/orders/2"
}
```

### 400 Bad Request (Validation Error - Empty Items)
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Authorization: Bearer <user_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "items": []
  }'
```

**Response:**
```json
{
  "timestamp": "2024-01-01T12:00:00",
  "status": 400,
  "error": "VALIDATION_ERROR",
  "message": "items must not be empty",
  "path": "/api/orders"
}
```

### 400 Bad Request (Validation Error - Invalid Quantity)
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Authorization: Bearer <user_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "items": [
      {
        "productId": 1,
        "quantity": -1
      }
    ]
  }'
```

**Response:**
```json
{
  "timestamp": "2024-01-01T12:00:00",
  "status": 400,
  "error": "VALIDATION_ERROR",
  "message": "quantity must be greater than 0",
  "path": "/api/orders"
}
```

---

## Quick Reference Table

| Method | Endpoint | Description | Requires Role |
|--------|----------|-------------|---------------|
| POST | `/api/orders` | Create new order | USER, PREMIUM_USER, ADMIN |
| GET | `/api/orders/my-orders` | Get my orders | USER, PREMIUM_USER, ADMIN |
| GET | `/api/orders/{id}` | Get order by ID | Owner or ADMIN |
| GET | `/api/orders` | Get all orders | ADMIN only |

---

## Discount Rules Summary

1. **USER Role:**
   - No discount (0%)
   - If order > $500: 5% discount applies

2. **PREMIUM_USER Role:**
   - 10% discount on all orders
   - If order > $500: Additional 5% discount (total 15%)

3. **Discount Calculation:**
   - Premium discount (10%): Applied if user is PREMIUM_USER
   - High order discount (5%): Applied if order total > $500
   - Discounts stack (both can apply for PREMIUM_USER with order > $500)

---

## Notes

1. **Stock Validation:** Orders are automatically rejected if insufficient stock
2. **Inventory Update:** Product quantities decrease automatically upon successful order
3. **Discount Application:** Discounts are calculated and applied automatically
4. **Order Ownership:** Users can only view their own orders (except ADMIN)
5. **Transaction Safety:** Order creation is transactional (all or nothing)
6. **Item Details:** Each order item includes productId, quantity, unitPrice, discountApplied, and totalPrice

