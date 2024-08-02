package com.example.ShopShoes.service.Impl;

import com.example.ShopShoes.dto.WishListDTO;
import com.example.ShopShoes.entity.WishList;
import com.example.ShopShoes.repository.ProductRepository;
import com.example.ShopShoes.repository.UserRepository;
import com.example.ShopShoes.repository.WishListRepository;
import com.example.ShopShoes.service.WishListService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WishListServiceImpl implements WishListService {

    private final WishListRepository wishListRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public WishListDTO createWishList(Long productId, String username) {
        WishList wishList = new WishList();
        wishList.setProduct(productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId)));
        wishList.setUser(userRepository.findByUsername(username));
        wishList.setQuantity(1);
        wishList = wishListRepository.save(wishList);
        return modelMapper.map(wishList, WishListDTO.class);
    }

    @Override
    public void deleteWishList(Long id) {
        wishListRepository.deleteById(id);
    }

    @Override
    public WishListDTO updateWishList(Long wishListId, WishListDTO wishListDTO) {
        WishList wishList = wishListRepository.findById(wishListId).orElseThrow(() -> new IllegalArgumentException("WishList not found with ID: " + wishListId));
        wishList.setQuantity(wishListDTO.getQuantity());
        wishList = wishListRepository.save(wishList);
        return modelMapper.map(wishList, WishListDTO.class);
    }

    @Override
    public WishListDTO getWishList(Long id) {
        WishList wishList = wishListRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("WishList not found with ID: " + id));
        return modelMapper.map(wishList, WishListDTO.class);
    }

    @Override
    public List<WishListDTO> getAllWishList() {
        List<WishList> wishLists = wishListRepository.findAll();
        return wishLists.stream().map(wishList -> modelMapper.map(wishList, WishListDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<WishListDTO> getWishListItemsByUser(String username) {
        List<WishList> wishLists = wishListRepository.findByUserUsername(username);
        return wishLists.stream().map(wishList -> modelMapper.map(wishList, WishListDTO.class)).collect(Collectors.toList());
    }


}
