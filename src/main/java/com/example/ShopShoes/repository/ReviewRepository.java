package com.example.ShopShoes.repository;

import com.example.ShopShoes.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.product.productId = ?1")
    List<Review> findAllByProductId(Long productId);

    @Query("SELECT r FROM Review r WHERE r.user.userId = ?1")
    List<Review> findAllByUserId(Long userId);

    //findByProductIdAndUserId(productId, userId)
    @Query("SELECT r FROM Review r WHERE r.product.productId = ?1 AND r.user.userId = ?2")
    Review findByProductIdAndUserId(Long productId, Long userId);

    @Query("SELECT u.username FROM User u WHERE u.userId = ?1")
    String findUsernameByUserId(Long userId);
}