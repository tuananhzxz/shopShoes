package com.example.ShopShoes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WishListDTO {
    private Long id;
    private Long userId;
    private Long productId;
    private Long productPrice;
    private String productName;
    private String productImage;
    private String productStock;
    private Integer quantity;
}
