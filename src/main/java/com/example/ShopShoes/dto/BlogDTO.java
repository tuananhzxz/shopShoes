package com.example.ShopShoes.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class BlogDTO {
    private Long blogId;
    private String title;
    private String content;
    private String image;
    private String author;
    private Timestamp createdAt;
    private String updatedAt;
    private String metaDescription;
    private String tags;
    private String category;
}
