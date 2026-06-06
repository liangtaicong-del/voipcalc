package com.voip.calc.domain.service;

import com.voip.calc.domain.model.CustomerType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Domain service — resolves the discount factor from customer identity.
 * VIP customers (overseas students/overseas Chinese card holders) receive 10% off.
 * Stateless and side-effect free.
 */
@Service
public class CustomerDiscountPolicyService {

    public BigDecimal resolveDiscountFactor(CustomerType customerType) {
        if (customerType == null) {
            return CustomerType.NORMAL.getDiscountFactor();
        }
        return customerType.getDiscountFactor();
    }
}
