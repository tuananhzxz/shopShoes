package com.example.ShopShoes.repository;

import com.example.ShopShoes.entity.CardItem;
import com.example.ShopShoes.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CardItemRepository extends JpaRepository<CardItem, Long> {

    @Query("SELECT c FROM CardItem c WHERE c.user.userId = ?1")
    List<CardItem> findAllByUserId(Long userId);

    @Query("SELECT c FROM CardItem c WHERE c.user.username = ?1")
    List<CardItem> findCardItemByUserName(String username);
}
