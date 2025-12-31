package com.example.ECommerceTask.service;

import com.example.ECommerceTask.domain.Enums.Role;
import com.example.ECommerceTask.domain.strategy.HighOrderDiscountStrategy;
import com.example.ECommerceTask.domain.strategy.PremiumUserDiscountStrategy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DiscountCalculatorService {

    private final PremiumUserDiscountStrategy premiumUserDiscountStrategy;
    private final HighOrderDiscountStrategy highOrderDiscountStrategy;

    public DiscountCalculatorService(
            PremiumUserDiscountStrategy premiumUserDiscountStrategy,
            HighOrderDiscountStrategy highOrderDiscountStrategy) {
        this.premiumUserDiscountStrategy = premiumUserDiscountStrategy;
        this.highOrderDiscountStrategy = highOrderDiscountStrategy;
    }

    /**
     * Calculates total discount based on user role and order total.
     * Discount rules:
     * - USER: no discount
     * - PREMIUM_USER: 10% off total order
     * - Orders > $500: extra 5% discount for any user (stacks with premium discount)
     *
     * @param orderTotal The subtotal before discounts
     * @param userRole The role of the user placing the order
     * @return Total discount amount
     */
    public BigDecimal calculateTotalDiscount(BigDecimal orderTotal, Role userRole) {
        BigDecimal totalDiscount = BigDecimal.ZERO;
        
        // Apply premium user discount (10% off) if user is PREMIUM_USER
        if (userRole == Role.PREMIUM_USER) {
            totalDiscount = totalDiscount.add(premiumUserDiscountStrategy.calculateDiscount(orderTotal));
        }
        
        // Apply high order discount (5% off) if order > $500 (applies to all users)
        BigDecimal highOrderDiscount = highOrderDiscountStrategy.calculateDiscount(orderTotal);
        totalDiscount = totalDiscount.add(highOrderDiscount);
        
        return totalDiscount;
    }
}

