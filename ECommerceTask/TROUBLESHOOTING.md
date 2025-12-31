# Troubleshooting: "No static resource" Error

## Error Message
```
{
    "status": 500,
    "error": "INTERNAL_SERVER_ERROR",
    "message": "No static resource api/orders for request '/api/orders'.",
    "path": "/api/orders",
    "timestamp": "2025-12-30T23:02:42.1428047"
}
```

## Root Cause

This error occurs when Spring Boot cannot find the controller mapping. It tries to serve the request as a static resource instead of routing it to the controller.

## Common Causes

1. **Application not recompiled** - The `OrderController` was added but the application wasn't rebuilt
2. **Application not restarted** - Changes require a full restart
3. **Bean creation failure** - A dependency issue prevents the controller from being registered
4. **Compilation errors** - The controller or its dependencies have compilation errors

## Solutions

### Solution 1: Clean and Rebuild (Most Common Fix)

**Using Maven Command Line:**
```bash
cd ECommerceTask
mvn clean
mvn compile
mvn spring-boot:run
```

**Using IDE (IntelliJ/Eclipse):**
1. Right-click on project → Maven → Reload Project
2. Build → Rebuild Project (or Clean and Build)
3. Stop the running application
4. Start the application again

### Solution 2: Check Application Startup Logs

When the application starts, look for these log messages:
```
Mapped "{[/api/orders],methods=[POST]}"
Mapped "{[/api/orders],methods=[GET]}"
Mapped "{[/api/orders/my-orders],methods=[GET]}"
Mapped "{[/api/orders/{id}],methods=[GET]}"
```

**If you DON'T see these mappings:**
- The controller is not being registered
- Check for bean creation errors in the logs
- Verify all dependencies are available

### Solution 3: Verify All Dependencies Are Available

Check that these beans are created successfully:
- `OrderService` (should be `OrderServiceImpl`)
- `OrderRepository`
- `DiscountCalculatorService`
- `ProductService`
- `UserRepository`
- `ProductRepository`

Look for errors like:
```
Error creating bean with name 'orderController'
```

### Solution 4: Check for Compilation Errors

**Using Maven:**
```bash
mvn clean compile
```

**Check for errors related to:**
- Missing imports
- Missing classes
- Type mismatches
- Missing dependencies

### Solution 5: Verify Package Structure

Ensure your controller is in the correct package:
- Main class: `com.example.ECommerceTask.ECommerceTaskApplication`
- Controller: `com.example.ECommerceTask.controller.OrderController`

The `@SpringBootApplication` annotation automatically scans sub-packages, so this should work.

### Solution 6: Check Database Migration

Ensure the database tables exist:
- `orders` table
- `order_items` table

Check if Flyway migration `V2__create_orders_tables.sql` ran successfully.

**Verify in logs:**
```
Flyway migration successful
```

### Solution 7: Verify Security Configuration

Ensure the endpoint is not being blocked by security. Check `SecurityConfig`:
- `/api/orders` should require authentication (not be in `permitAll()`)

### Solution 8: Check Request Method

Ensure you're using the correct HTTP method:
- `POST /api/orders` - Create order
- `GET /api/orders` - Get all orders (Admin only)
- `GET /api/orders/my-orders` - Get my orders
- `GET /api/orders/{id}` - Get order by ID

### Solution 9: Verify JWT Token

If you're getting this error with authentication, ensure:
- Token is valid and not expired
- Token is in correct format: `Bearer <token>`
- User has appropriate role (USER, PREMIUM_USER, or ADMIN for orders)

## Quick Diagnostic Steps

1. **Stop the application completely**
2. **Clean build:**
   ```bash
   mvn clean compile
   ```
3. **Check for compilation errors**
4. **Start the application**
5. **Check startup logs for controller mappings**
6. **Test the endpoint again**

## Expected Behavior After Fix

After rebuilding and restarting, you should see in the logs:
```
Mapped "{[/api/orders],methods=[POST],produces=[application/json]}"
Mapped "{[/api/orders],methods=[GET],produces=[application/json]}"
Mapped "{[/api/orders/my-orders],methods=[GET],produces=[application/json]}"
Mapped "{[/api/orders/{id}],methods=[GET],produces=[application/json]}"
```

And the endpoint should work correctly.

## Still Not Working?

If the issue persists after trying all solutions:

1. **Check application logs** for any bean creation errors
2. **Verify all order-related classes are compiled** in the `target/classes` directory
3. **Try accessing a working endpoint** (like `/api/products`) to verify the application is running correctly
4. **Check if other controllers work** - if they do, the issue is specific to OrderController

