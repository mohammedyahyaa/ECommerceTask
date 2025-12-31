# E-Commerce REST API

A Spring Boot-based REST API for managing products, users, and orders with JWT authentication and role-based access control.

## Features

- **User Management**: Registration, authentication, and user CRUD operations
- **Product Management**: Full CRUD operations with search and filtering
- **Order Management**: Order placement with stock validation and automatic discount calculation
- **JWT Authentication**: Secure token-based authentication
- **Role-Based Access Control**: USER, PREMIUM_USER, and ADMIN roles with different permissions
- **Discount System**: Dynamic discount calculation using Strategy pattern
- **Data Persistence**: File-based H2 database for data persistence

## Technology Stack

- **Framework**: Spring Boot 4.0.1
- **Language**: Java 21
- **Database**: H2 (File-based)
- **Security**: Spring Security with JWT
- **Build Tool**: Maven
- **Migration**: Flyway

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.6+

### Running the Application

1. **Clone the repository** (if applicable) or navigate to the project directory

2. **Build the project**:
   ```bash
   cd ECommerceTask
   mvn clean install
   ```

3. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**:
   - API Base URL: `http://localhost:8080`
   - H2 Console: `http://localhost:8080/h2-console`
     - JDBC URL: `jdbc:h2:file:./data/ecommerce`
     - Username: `sa`
     - Password: (empty)

## API Endpoints

### Authentication (Public)

- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login and get JWT token

### User Management (Admin Only)

- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Product Management

- `GET /api/products` - Get all products (with filters) - All authenticated users
- `GET /api/products/{id}` - Get product by ID - All authenticated users
- `POST /api/products` - Create product - Admin only
- `PUT /api/products/{id}` - Update product - Admin only
- `DELETE /api/products/{id}` - Delete product (soft delete) - Admin only

### Order Management

- `POST /api/orders` - Create order - USER, PREMIUM_USER, ADMIN
- `GET /api/orders/my-orders` - Get my orders - All authenticated users
- `GET /api/orders/{id}` - Get order by ID - Owner or Admin
- `GET /api/orders` - Get all orders - Admin only

## Complete Workflow Examples

### 1. Product Management Cycle

#### Step 1: Register Admin User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123",
    "role": "ADMIN"
  }'
```

#### Step 2: Login as Admin
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```
**Save the token from the response!**

#### Step 3: Create Products
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

curl -X POST http://localhost:8080/api/products \
  -H "Authorization: Bearer <admin_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Mouse",
    "description": "Wireless mouse",
    "price": 29.99,
    "quantity": 100
  }'
```

#### Step 4: View Products
```bash
# Get all products
curl -X GET http://localhost:8080/api/products \
  -H "Authorization: Bearer <admin_token>"

# Get product by ID
curl -X GET http://localhost:8080/api/products/1 \
  -H "Authorization: Bearer <admin_token>"

# Search products
curl -X GET "http://localhost:8080/api/products?name=laptop&minPrice=500&maxPrice=1500" \
  -H "Authorization: Bearer <admin_token>"
```

#### Step 5: Update Product
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

#### Step 6: Delete Product (Soft Delete)
```bash
curl -X DELETE http://localhost:8080/api/products/1 \
  -H "Authorization: Bearer <admin_token>"
```

---

### 2. User Management Cycle

#### Step 1: Login as Admin
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```
**Save the admin token!**

#### Step 2: Create Users
```bash
# Create regular user
curl -X POST http://localhost:8080/api/users \
  -H "Authorization: Bearer <admin_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user1",
    "password": "password123",
    "role": "USER"
  }'

# Create premium user
curl -X POST http://localhost:8080/api/users \
  -H "Authorization: Bearer <admin_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "premium1",
    "password": "password123",
    "role": "PREMIUM_USER"
  }'
```

#### Step 3: View All Users
```bash
curl -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer <admin_token>"
```

#### Step 4: Get User by ID
```bash
curl -X GET http://localhost:8080/api/users/2 \
  -H "Authorization: Bearer <admin_token>"
```

#### Step 5: Update User
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

#### Step 6: Delete User
```bash
curl -X DELETE http://localhost:8080/api/users/3 \
  -H "Authorization: Bearer <admin_token>"
```

---

### 3. Order Management Cycle

#### Step 1: Register/Login as User
```bash
# Register user
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "customer1",
    "password": "password123",
    "role": "USER"
  }'

# Login to get token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "customer1",
    "password": "password123"
  }'
```
**Save the user token!**

#### Step 2: View Available Products
```bash
curl -X GET http://localhost:8080/api/products \
  -H "Authorization: Bearer <user_token>"
```

#### Step 3: Create Order
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

**Order Process:**
- Validates stock availability
- Calculates discounts based on user role and order total
- Decreases product inventory automatically
- Creates order with order items
- Returns order details with applied discounts

#### Step 4: View My Orders
```bash
curl -X GET http://localhost:8080/api/orders/my-orders \
  -H "Authorization: Bearer <user_token>"
```

#### Step 5: Get Order by ID
```bash
curl -X GET http://localhost:8080/api/orders/1 \
  -H "Authorization: Bearer <user_token>"
```

#### Step 6: View All Orders (Admin Only)
```bash
curl -X GET http://localhost:8080/api/orders \
  -H "Authorization: Bearer <admin_token>"
```

---

## Discount Rules

The system automatically applies discounts based on user role and order total:

1. **USER Role:**
   - No discount (0%)
   - Orders > $500: 5% discount

2. **PREMIUM_USER Role:**
   - 10% discount on all orders
   - Orders > $500: Additional 5% discount (total 15% discount)

3. **Discount Calculation:**
   - Premium discount (10%): Applied if user is PREMIUM_USER
   - High order discount (5%): Applied if order total > $500
   - Discounts stack (both can apply for PREMIUM_USER with order > $500)

## Roles and Permissions

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

## Database

The application uses H2 file-based database for data persistence. The database file is stored at:
- **Location**: `./data/ecommerce.mv.db` (relative to application root)
- **Database persists across application restarts**

### Accessing H2 Console

1. Start the application
2. Navigate to: `http://localhost:8080/h2-console`
3. Use these connection details:
   - JDBC URL: `jdbc:h2:file:./data/ecommerce`
   - Username: `sa`
   - Password: (leave empty)

## Data Models

### User
- id, username, password, role

### Product
- id, name, description, price, quantity, deleted (soft delete), timestamps

### Order
- id, userId, orderTotal, createdAt
- Contains OrderItems (productId, quantity, unitPrice, discountApplied, totalPrice)

## Authentication

All endpoints except `/api/auth/**` require JWT authentication. Include the token in the Authorization header:

```
Authorization: Bearer <your_jwt_token>
```

**Token expires after 24 hours.** Login again to get a new token.

## Project Structure

```
ECommerceTask/
├── src/
│   ├── main/
│   │   ├── java/com/example/ECommerceTask/
│   │   │   ├── config/          # Security configuration
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── domain/
│   │   │   │   ├── Entity/      # JPA entities
│   │   │   │   ├── Enums/       # Enum definitions
│   │   │   │   └── strategy/    # Discount strategies
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── exception/       # Exception handling
│   │   │   ├── repository/      # Data access layer
│   │   │   ├── security/        # Security components
│   │   │   └── service/         # Business logic
│   │   └── resources/
│   │       ├── db/migration/    # Flyway migrations
│   │       └── application.properties
│   └── test/                    # Test files
└── pom.xml
```

## Additional Documentation

- `API_TESTING_GUIDE.md` - Complete API testing guide with examples
- `USER_MANAGEMENT_API_CURL.md` - User management cURL commands
- `ORDER_MANAGEMENT_API_CURL.md` - Order management cURL commands
- `AUTH_SEQUENCE.md` - Authentication sequence guide
- `TROUBLESHOOTING.md` - Common issues and solutions

## Notes

- **Soft Delete**: Products are soft deleted (marked as deleted, not physically removed)
- **Stock Validation**: Orders are rejected if insufficient stock
- **Inventory Update**: Product quantities decrease automatically on successful order
- **Transaction Safety**: Order creation is transactional (all or nothing)
- **Password Security**: Passwords are hashed using BCrypt



