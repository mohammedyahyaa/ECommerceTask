package com.example.ECommerceTask.repository;

import com.example.ECommerceTask.domain.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByPriceBetween(BigDecimal min, BigDecimal max);

    List<Product> findByQuantityGreaterThan(Integer quantity);
}
