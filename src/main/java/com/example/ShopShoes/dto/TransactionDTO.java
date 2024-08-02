package com.example.ShopShoes.dto;

import com.example.ShopShoes.entity.Transaction;
import com.example.ShopShoes.entity.User;
import com.example.ShopShoes.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private Long id;
    private int status;
    private Long userId;
    private String firstName;
    private String lastName;
    private String guestEmail;
    private String guestPhone;
    private String guestAddress;
    private String city;
    private String company;
    private String zipCode;
    private String message;
    private BigDecimal amount;
    private String payment;
    private Timestamp created;
    private List<OrderDTO> orders;
    private String couponCode;
}