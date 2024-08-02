package com.example.ShopShoes.service;

import com.example.ShopShoes.dto.ShippingOptionDTO;
import com.example.ShopShoes.entity.ShippingOption;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShippingOptionService {

    List<ShippingOptionDTO> getAllShippingOptions();

}
