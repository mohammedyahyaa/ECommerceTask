package com.example.ECommerceTask.domain.strategy;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class NoDiscountStrategy implements DiscountStrategy {

    @Override
    public BigDecimal calculateDiscount(BigDecimal orderTotal) {
        return BigDecimal.ZERO;
    }

    @Override
    public String getStrategyName() {
        return "NO_DISCOUNT";
    }
}

