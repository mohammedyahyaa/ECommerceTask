package com.example.ECommerceTask.domain.strategy;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class HighOrderDiscountStrategy implements DiscountStrategy {

    private static final BigDecimal HIGH_ORDER_THRESHOLD = new BigDecimal("500.00");
    private static final BigDecimal HIGH_ORDER_DISCOUNT_PERCENTAGE = new BigDecimal("0.05"); // 5%

    @Override
    public BigDecimal calculateDiscount(BigDecimal orderTotal) {
        if (orderTotal.compareTo(HIGH_ORDER_THRESHOLD) > 0) {
            return orderTotal.multiply(HIGH_ORDER_DISCOUNT_PERCENTAGE);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getStrategyName() {
        return "HIGH_ORDER_5_PERCENT";
    }
}

