package com.example.ShopShoes.repository;

import com.example.ShopShoes.entity.ShippingOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ShippingOptionRepository extends JpaRepository<ShippingOption, Long> {

}
