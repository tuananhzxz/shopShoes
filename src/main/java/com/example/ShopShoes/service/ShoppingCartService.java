package com.example.ShopShoes.service;

import com.example.ShopShoes.dto.CardItemDTO;

import java.util.List;

public interface ShoppingCartService {
    CardItemDTO addItemToCart(Long productId, Integer quantity, String username);
    void removeItemFromCart(Long cardItemId);
    void updateCartItemQuantity(Long cardItemId, Integer quantity);
    List<CardItemDTO> getCartItemsByUser(String username);
    void updateCartShippingOptionForUser(String username, Long shippingOptionId);
    void clearCart(String username);
    CardItemDTO getCardItemById(Long id);

}