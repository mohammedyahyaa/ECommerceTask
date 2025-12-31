package com.example.ECommerceTask.service;

import com.example.ECommerceTask.dto.product.ProductRequest;
import com.example.ECommerceTask.dto.product.ProductResponse;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    ProductResponse create(ProductRequest request);

    ProductResponse update(Long id, ProductRequest request);

    void delete(Long id);

    ProductResponse getById(Long id);

    List<ProductResponse> getAll();

    List<ProductResponse> search(
            String name,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean available
    );

    void decreaseInventory(Long productId, Integer quantity);
}
