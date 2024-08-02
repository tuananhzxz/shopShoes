package com.example.ShopShoes.dto;

import com.example.ShopShoes.entity.CardItem;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
public class CardItemDTO {
    private Long cardItemId;
    private Long productId;
    private String name;
    private String description;
    private BigDecimal price;
    private String size;
    private String color;
    private Integer stock;
    private Timestamp createdAt;
    private String image;
    private Long categoryId;
    private String categoryName;
    private Integer quantity;
    private BigDecimal totalPrice;
    private String username;
    private Long userId;
    private String shippingOptionName;

    public CardItemDTO(CardItem cardItem) {
        this.cardItemId = cardItem.getCardItemId();
        this.productId = cardItem.getProduct().getProductId();
        this.name = cardItem.getProduct().getName();
        this.description = cardItem.getProduct().getDescription();
        this.price = cardItem.getProduct().getPrice();
        this.size = cardItem.getProduct().getSize();
        this.color = cardItem.getProduct().getColor();
        this.stock = cardItem.getProduct().getStock();
        this.createdAt = cardItem.getProduct().getCreatedAt();
        this.image = cardItem.getProduct().getImage();
        if (cardItem.getProduct().getCategory() != null) {
            this.categoryId = cardItem.getProduct().getCategory().getId();
            this.categoryName = cardItem.getProduct().getCategory().getName();
        }
        this.quantity = cardItem.getQuantity();
        this.totalPrice = cardItem.getTotalPrice();
        if (cardItem.getUser() != null) {
            this.username = cardItem.getUser().getUsername();
        }
        if (cardItem.getUser() != null) {
            this.userId = cardItem.getUser().getUserId();
        }
    }
}