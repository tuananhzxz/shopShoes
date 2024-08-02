package com.example.ShopShoes.dto;

import com.example.ShopShoes.entity.Order;
import com.example.ShopShoes.entity.User;
import com.example.ShopShoes.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long orderId;
    private Long userId;
    private String productName;
    private String quantity;
    private BigDecimal totalAmount;
    private String status;
    private Timestamp createdAt;
    private String address;
    private String paymentMethod;
    private String message;
    private Long transactionId;
}