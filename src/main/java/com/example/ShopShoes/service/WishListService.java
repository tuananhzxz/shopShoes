package com.example.ShopShoes.service;

import com.example.ShopShoes.dto.WishListDTO;

import java.util.List;

public interface WishListService {

    WishListDTO createWishList(Long productId, String username);

    void deleteWishList(Long id);

    WishListDTO updateWishList(Long id, WishListDTO wishListDTO);

    WishListDTO getWishList(Long id);

    List<WishListDTO> getAllWishList();

    List<WishListDTO> getWishListItemsByUser(String username);
}
