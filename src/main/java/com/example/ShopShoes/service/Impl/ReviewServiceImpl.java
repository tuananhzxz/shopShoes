package com.example.ShopShoes.service.Impl;

import com.example.ShopShoes.dto.ReviewDTO;
import com.example.ShopShoes.entity.Review;
import com.example.ShopShoes.repository.ReviewRepository;
import com.example.ShopShoes.service.ReviewService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;

    @Override
    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        Review review = modelMapper.map(reviewDTO, Review.class);
        Review savedReview = reviewRepository.save(review);
        return modelMapper.map(savedReview, ReviewDTO.class);
    }

    @Override
    public List<ReviewDTO> getReviewByProductId(Long productId) {
        List<Review> reviews = reviewRepository.findAllByProductId(productId);
        return reviews.stream().map(review -> {
            ReviewDTO dto = new ReviewDTO();
            dto.setReviewId(review.getReviewId());
            dto.setProductId(review.getProduct().getProductId());
            dto.setUserId(review.getUser().getUserId());
            dto.setComment(review.getComment());
            dto.setRating(review.getRating());
            dto.setCreatedAt(review.getCreatedAt());
            dto.setUsername(reviewRepository.findUsernameByUserId(review.getUser().getUserId()));
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ReviewDTO> getReviewByUserId(Long userId) {
        List<Review> reviews = reviewRepository.findAllByUserId(userId);
        return reviews.stream().map(review -> modelMapper.map(review, ReviewDTO.class)).toList();
    }

    @Override
    public ReviewDTO getReviewByReviewId(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RuntimeException("Review not found"));
        return modelMapper.map(review, ReviewDTO.class);
    }

    @Override
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    @Override
    public ReviewDTO getUserRatingForProduct(Long userId, Long productId) {
        Review review = reviewRepository.findByProductIdAndUserId(productId, userId);
        return review != null ? modelMapper.map(review, ReviewDTO.class) : null;
    }

    @Override
    public List<ReviewDTO> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream().map(review -> modelMapper.map(review, ReviewDTO.class)).toList();
    }
}
