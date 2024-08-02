package com.example.ShopShoes.repository;

import com.example.ShopShoes.entity.Order;
import com.example.ShopShoes.entity.Product;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>{

    Page<Order> findByStatusContaining(String status, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.user.userId = ?1")
    List<Order> findByUserId(Long userId);

    @Query("SELECT o FROM Order o ORDER BY o.createdAt DESC, Rand() LIMIT 12")
    List<Order> findTop7ByOrderByCreatedAtDesc();

}
