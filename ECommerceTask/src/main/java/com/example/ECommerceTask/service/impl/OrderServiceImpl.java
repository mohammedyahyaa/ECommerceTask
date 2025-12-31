package com.example.ECommerceTask.service.impl;

import com.example.ECommerceTask.domain.Entity.Order;
import com.example.ECommerceTask.domain.Entity.OrderItem;
import com.example.ECommerceTask.domain.Entity.Product;
import com.example.ECommerceTask.domain.Entity.User;
import com.example.ECommerceTask.domain.Enums.Role;
import com.example.ECommerceTask.dto.order.OrderItemRequest;
import com.example.ECommerceTask.dto.order.OrderItemResponse;
import com.example.ECommerceTask.dto.order.OrderRequest;
import com.example.ECommerceTask.dto.order.OrderResponse;
import com.example.ECommerceTask.exception.ResourceNotFoundException;
import com.example.ECommerceTask.repository.OrderRepository;
import com.example.ECommerceTask.repository.ProductRepository;
import com.example.ECommerceTask.repository.UserRepository;
import com.example.ECommerceTask.service.DiscountCalculatorService;
import com.example.ECommerceTask.service.OrderService;
import com.example.ECommerceTask.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final DiscountCalculatorService discountCalculatorService;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            UserRepository userRepository,
            ProductRepository productRepository,
            ProductService productService,
            DiscountCalculatorService discountCalculatorService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productService = productService;
        this.discountCalculatorService = discountCalculatorService;
    }

    @Override
    @Transactional
    public OrderResponse create(OrderRequest request, String username) {
        // Get user
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Validate stock and calculate subtotal
        BigDecimal subtotal = BigDecimal.ZERO;
        List<OrderItem> orderItems = new java.util.ArrayList<>();

        for (OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + itemRequest.getProductId()));

            // Validate stock
            if (product.getQuantity() < itemRequest.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for product: " + product.getName() +
                        ". Available: " + product.getQuantity() + ", Requested: " + itemRequest.getQuantity());
            }

            BigDecimal unitPrice = product.getPrice();
            BigDecimal itemSubtotal = unitPrice.multiply(new BigDecimal(itemRequest.getQuantity()));
            subtotal = subtotal.add(itemSubtotal);
        }

        // Calculate discounts
        BigDecimal totalDiscount = discountCalculatorService.calculateTotalDiscount(subtotal, user.getRole());
        BigDecimal orderTotal = subtotal.subtract(totalDiscount);

        // Create order
        Order order = new Order(user, orderTotal);
        order = orderRepository.save(order);

        // Create order items and decrease inventory
        for (OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            BigDecimal unitPrice = product.getPrice();
            BigDecimal itemSubtotal = unitPrice.multiply(new BigDecimal(itemRequest.getQuantity()));
            
            // Calculate discount per item (proportional to item's share of subtotal)
            BigDecimal itemDiscount = BigDecimal.ZERO;
            if (totalDiscount.compareTo(BigDecimal.ZERO) > 0 && subtotal.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal itemProportion = itemSubtotal.divide(subtotal, 4, RoundingMode.HALF_UP);
                itemDiscount = totalDiscount.multiply(itemProportion);
            }
            
            BigDecimal itemTotal = itemSubtotal.subtract(itemDiscount);

            OrderItem orderItem = new OrderItem(order, product, itemRequest.getQuantity(),
                    unitPrice, itemDiscount, itemTotal);
            orderItems.add(orderItem);

            // Decrease inventory
            productService.decreaseInventory(product.getId(), itemRequest.getQuantity());
        }

        order.setItems(orderItems);
        order = orderRepository.save(order);

        return mapToResponse(order);
    }

    @Override
    public OrderResponse getById(Long id, String username) {
        Order order = orderRepository.findByIdWithItems(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Check if user owns the order or is admin
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!order.getUser().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new IllegalArgumentException("You don't have permission to view this order");
        }

        return mapToResponse(order);
    }

    @Override
    public List<OrderResponse> getOrdersByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return orderRepository.findByUserIdWithItems(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private OrderResponse mapToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setUserId(order.getUser().getId());
        response.setUsername(order.getUser().getUsername());
        response.setOrderTotal(order.getOrderTotal());
        response.setCreatedAt(order.getCreatedAt());

        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(this::mapItemToResponse)
                .collect(Collectors.toList());
        response.setItems(itemResponses);

        return response;
    }

    private OrderItemResponse mapItemToResponse(OrderItem item) {
        OrderItemResponse response = new OrderItemResponse();
        response.setId(item.getId());
        response.setProductId(item.getProduct().getId());
        response.setProductName(item.getProduct().getName());
        response.setQuantity(item.getQuantity());
        response.setUnitPrice(item.getUnitPrice());
        response.setDiscountApplied(item.getDiscountApplied());
        response.setTotalPrice(item.getTotalPrice());
        return response;
    }
}

