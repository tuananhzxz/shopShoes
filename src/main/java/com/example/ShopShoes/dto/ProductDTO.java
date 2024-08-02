package com.example.ShopShoes.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long productId;
    private String name;
    private String description;
    private BigDecimal price;
    private String size;
    private String color;
    private Integer stock;
    private Timestamp createdAt;
    private Long categoryId;
    private String categoryName;
    private String image;
    private MultipartFile imageFile;
    private Integer reviewCount;
    private Double averageRating;
}

