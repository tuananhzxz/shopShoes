package com.example.ShopShoes.dto;
import java.sql.Timestamp;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private Long reviewId;
    private Long productId;
    private String username;
    private Long userId;
    private Integer rating;
    private String comment;
    private Timestamp createdAt;
}
