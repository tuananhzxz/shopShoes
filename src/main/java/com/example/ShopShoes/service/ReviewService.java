package com.example.ShopShoes.service;

import com.example.ShopShoes.dto.ReviewDTO;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    ReviewDTO createReview(ReviewDTO reviewDTO);

    List<ReviewDTO> getReviewByProductId(Long productId);

    List<ReviewDTO> getReviewByUserId(Long userId);

    ReviewDTO getReviewByReviewId(Long reviewId);

    void deleteReview(Long reviewId);
    ReviewDTO getUserRatingForProduct(Long userId, Long productId);

    List<ReviewDTO> getAllReviews();
}
