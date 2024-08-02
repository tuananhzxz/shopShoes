package com.example.ShopShoes.controller.Product;

import com.example.ShopShoes.controller.Back.HomeController;
import com.example.ShopShoes.dto.ProductDTO;
import com.example.ShopShoes.dto.ReviewDTO;
import com.example.ShopShoes.service.CategoryService;
import com.example.ShopShoes.service.ProductService;
import com.example.ShopShoes.service.ReviewService;
import com.example.ShopShoes.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping()
public class ProductFEController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final ProductFunction productFunction;
    private final ReviewService reviewService;
    private final UserService userService;

    @GetMapping("/product{id}")
    public String showProduct(@PathVariable("id") Long productId, Model model, Principal principal) {
        productFunction.showProduct(productId, model, productService, categoryService);

        List<ReviewDTO> reviews = reviewService.getReviewByProductId(productId);
        model.addAttribute("reviews", reviews);
        model.addAttribute("reviewCount", reviews.size());

        Long userId;
        if (principal != null) {
            userId = userService.getUserByUsername(principal.getName()).getUserId();
        } else {
            userId = getCurrentUserId();
        }
        ReviewDTO userRating = reviewService.getUserRatingForProduct(userId, productId);
        if (userRating != null) {
            model.addAttribute("userRating", userRating.getRating());
        } else {
            model.addAttribute("userRating", 0);
        }

        List<ProductDTO> randomProducts = productService.getRandomProduct();
        HomeController.ReviewCount(randomProducts, reviewService);
        model.addAttribute("randomProducts", randomProducts);

        return "fe/product";

    }

    @PostMapping("/product")
    @ResponseBody
    public String rateProduct(@RequestParam Long productId, @RequestParam int rating, Principal principal) {
        ReviewDTO reviewDTO = new ReviewDTO();
        Long userId;
        if (principal != null) {
            userId = userService.getUserByUsername(principal.getName()).getUserId();
        } else {
            userId = getCurrentUserId();
        }
        ReviewDTO existingReview = reviewService.getUserRatingForProduct(userId, productId);
        if (existingReview != null) {
            if (rating < 1 || rating > 5) {
                rating = 5;
            }
            existingReview.setRating(rating);
            reviewService.createReview(existingReview);
        } else {
            reviewDTO.setProductId(productId);
            reviewDTO.setUserId(userId);
            if (rating < 1 || rating > 5) {
                rating = 5;
            }
            reviewDTO.setRating(rating);
            reviewService.createReview(reviewDTO);
        }

        return "Cám ơn bạn đã đánh giá";
    }

    @PostMapping("/product{id}")
    public String addComment(@PathVariable("id") Long productId,
                             @RequestParam(required = false) Integer rating,
                             @RequestParam String comment,
                             Principal principal,
                             Model model) {
        Long userId;
        if (principal != null) {
            userId = userService.getUserByUsername(principal.getName()).getUserId();
        } else {
            userId = getCurrentUserId();
        }

        if (rating == null) {
            rating = 5; // Set a default rating if none provided
        } else if (rating < 1 || rating > 5) {
            rating = 5; // Ensure rating is within valid range (1 to 5)
        }

        ReviewDTO existingReview = reviewService.getUserRatingForProduct(userId, productId);
        if (existingReview != null) {
            existingReview.setComment(comment);
            existingReview.setRating(rating);
            reviewService.createReview(existingReview);
        } else {
            ReviewDTO reviewDTO = new ReviewDTO();
            reviewDTO.setProductId(productId);
            reviewDTO.setUserId(userId);
            reviewDTO.setComment(comment);
            reviewDTO.setRating(rating);
            reviewService.createReview(reviewDTO);
        }

        return "redirect:/product" + productId;
    }

    private Long getCurrentUserId() {
        return 1L;
    }

}
