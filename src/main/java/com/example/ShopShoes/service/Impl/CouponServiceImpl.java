package com.example.ShopShoes.service.Impl;

import com.example.ShopShoes.entity.Coupon;
import com.example.ShopShoes.entity.CouponCondition;
import com.example.ShopShoes.repository.CouponConditionRepository;
import com.example.ShopShoes.repository.CouponRepository;
import com.example.ShopShoes.service.CouponService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CouponConditionRepository couponConditionRepository;

    @Override
    public BigDecimal calculateValue(String couponCode, BigDecimal totalAmount) {
        Coupon coupon = couponRepository.findByCode(couponCode)
                .orElseThrow(() -> new RuntimeException("Coupon not found"));

        if (!coupon.getActive()) {
            throw new RuntimeException("Coupon is not active");
        }

        BigDecimal discount = calculateDiscount(coupon, totalAmount);
        return totalAmount.subtract(discount);
    }

    private BigDecimal calculateDiscount(Coupon coupon, BigDecimal totalAmount) {
        List<CouponCondition> couponConditions = couponConditionRepository.findByCouponId(coupon.getId());
        BigDecimal discount = BigDecimal.ZERO;

        for (CouponCondition couponCondition : couponConditions) {
            String attribute = couponCondition.getAttribute();
            String operator = couponCondition.getOperator();
            String value = couponCondition.getValue();
            BigDecimal percentDiscount = couponCondition.getDiscountAmount();

            if ("minimum_amount".equals(attribute)) {
                BigDecimal amountValue = new BigDecimal(value);
                int compareResult = totalAmount.compareTo(amountValue);

                if ((">".equals(operator) && compareResult > 0)
                        || ("<".equals(operator) && compareResult < 0)
                        || ("=".equals(operator) && compareResult == 0)
                        || (">=".equals(operator) && compareResult >= 0)
                        || ("<=".equals(operator) && compareResult <= 0)
                        || ("!=".equals(operator) && compareResult != 0)) {
                    discount = discount.add(totalAmount.multiply(percentDiscount.divide(BigDecimal.valueOf(100))));
                }
            } else if ("applicable_date".equals(attribute)) {
                LocalDate currentDate = LocalDate.now();
                LocalDate applicableDate = LocalDate.parse(value);

                if ("BETWEEN".equalsIgnoreCase(operator) && currentDate.isEqual(applicableDate)) {
                    discount = discount.add(totalAmount.multiply(percentDiscount.divide(BigDecimal.valueOf(100))));
                } else if ("BEFORE".equalsIgnoreCase(operator) && currentDate.isBefore(applicableDate)) {
                    discount = discount.add(totalAmount.multiply(percentDiscount.divide(BigDecimal.valueOf(100))));
                } else if ("AFTER".equalsIgnoreCase(operator) && currentDate.isAfter(applicableDate)) {
                    discount = discount.add(totalAmount.multiply(percentDiscount.divide(BigDecimal.valueOf(100))));
                }
            }
        }

        return discount;
    }

}
