package com.example.ECommerceTask.domain.strategy;

import java.math.BigDecimal;

public interface DiscountStrategy {
    BigDecimal calculateDiscount(BigDecimal orderTotal);
    String getStrategyName();
}

