package com.example.ShopShoes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String message;
    private String status;
    private String subject;
    private String createdAt;
}
