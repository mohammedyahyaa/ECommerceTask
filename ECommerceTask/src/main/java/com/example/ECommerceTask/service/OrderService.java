package com.example.ECommerceTask.service;

import com.example.ECommerceTask.dto.order.OrderRequest;
import com.example.ECommerceTask.dto.order.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse create(OrderRequest request, String username);

    OrderResponse getById(Long id, String username);

    List<OrderResponse> getOrdersByUser(String username);

    List<OrderResponse> getAllOrders();
}

