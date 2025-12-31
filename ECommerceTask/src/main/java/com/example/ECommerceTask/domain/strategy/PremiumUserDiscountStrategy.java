package com.example.ECommerceTask.domain.strategy;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PremiumUserDiscountStrategy implements DiscountStrategy {

    private static final BigDecimal PREMIUM_DISCOUNT_PERCENTAGE = new BigDecimal("0.10"); // 10%

    @Override
    public BigDecimal calculateDiscount(BigDecimal orderTotal) {
        return orderTotal.multiply(PREMIUM_DISCOUNT_PERCENTAGE);
    }

    @Override
    public String getStrategyName() {
        return "PREMIUM_USER_10_PERCENT";
    }
}

