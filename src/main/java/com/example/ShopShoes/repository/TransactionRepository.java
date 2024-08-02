package com.example.ShopShoes.repository;

import com.example.ShopShoes.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.user.userId = ?1")
    List<Transaction> findByUserId(Long id);

}
