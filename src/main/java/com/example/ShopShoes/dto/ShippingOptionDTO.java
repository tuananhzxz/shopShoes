package com.example.ShopShoes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShippingOptionDTO {
    private Long shippingOptionId;
    private String name;
    private String fee;
}
