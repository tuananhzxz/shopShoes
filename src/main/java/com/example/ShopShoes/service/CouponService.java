package com.example.ShopShoes.service;

import java.math.BigDecimal;
import java.util.List;

public interface CouponService {
    BigDecimal calculateValue(String couponCode, BigDecimal totalAmount);
}
