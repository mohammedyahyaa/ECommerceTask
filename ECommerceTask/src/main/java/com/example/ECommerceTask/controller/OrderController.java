package com.example.ECommerceTask.controller;

import com.example.ECommerceTask.dto.order.OrderRequest;
import com.example.ECommerceTask.dto.order.OrderResponse;
import com.example.ECommerceTask.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // ---------------- CREATE ORDER (USER, PREMIUM_USER, ADMIN) ----------------
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'PREMIUM_USER', 'ADMIN')")
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest request) {

        String username = getCurrentUsername();
        OrderResponse response = orderService.create(request, username);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // ---------------- GET ORDER BY ID (Owner or ADMIN) ----------------
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'PREMIUM_USER', 'ADMIN')")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        String username = getCurrentUsername();
        return ResponseEntity.ok(orderService.getById(id, username));
    }

    // ---------------- GET USER'S ORDERS (USER, PREMIUM_USER, ADMIN) ----------------
    @GetMapping("/my-orders")
    @PreAuthorize("hasAnyRole('USER', 'PREMIUM_USER', 'ADMIN')")
    public ResponseEntity<List<OrderResponse>> getMyOrders() {
        String username = getCurrentUsername();
        return ResponseEntity.ok(orderService.getOrdersByUser(username));
    }

    // ---------------- GET ALL ORDERS (ADMIN only) ----------------
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof String) {
            return (String) authentication.getPrincipal();
        }
        throw new IllegalStateException("User not authenticated");
    }
}

