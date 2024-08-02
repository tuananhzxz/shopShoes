package com.example.ShopShoes.repository;

import com.example.ShopShoes.dto.WishListDTO;
import com.example.ShopShoes.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WishListRepository extends JpaRepository<WishList, Long> {

    @Query("SELECT w FROM WishList w WHERE w.user.username = ?1")
    List<WishList> findByUserUsername(String username);
}
