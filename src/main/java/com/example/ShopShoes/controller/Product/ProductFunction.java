package com.example.ShopShoes.controller.Product;

import com.example.ShopShoes.dto.CategoryDTO;
import com.example.ShopShoes.dto.ProductDTO;
import com.example.ShopShoes.dto.ReviewDTO;
import com.example.ShopShoes.entity.Product;
import com.example.ShopShoes.service.CategoryService;
import com.example.ShopShoes.service.ProductService;
import com.example.ShopShoes.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ProductFunction {

    private final ProductService productService;
    private final ReviewService reviewService;

    public void getAllProduct(int page, int size, String keyword, Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDTO> productPage;
        if (keyword != null && !keyword.isEmpty()) {
            productPage = productService.searchProduct(keyword, pageable);
        } else {
            productPage = productService.getAllProduct(pageable);
        }
        model.addAttribute("productPage", productPage);
        model.addAttribute("keyword", keyword);
    }

    public void showProduct(@PathVariable("id") Long productId, Model model, ProductService productService, CategoryService categoryService) {
        ProductDTO productDTO = productService.getProductById(productId);
        model.addAttribute("productDTO", productDTO);
        List<CategoryDTO> listCate = categoryService.getAllCategories();
        model.addAttribute("listCate", listCate);

        String existingSizesString = productService.getAllSizesAsString(productId);
        List<String> existingSizes = Arrays.asList(existingSizesString.split(","));
        model.addAttribute("existingSizes", existingSizes);

        List<ProductDTO> randomProduct = productService.getRandomProduct();
        model.addAttribute("randomProduct", randomProduct);

        List<ProductDTO> productDTOList = productService.getProductByCategory(productDTO.getCategoryName());
        model.addAttribute("productDTOList", productDTOList);
    }

    public void getAllProduct(int page, int size, String keyword, Model model, String category) {
        Page<ProductDTO> productPage;
        if (category != null && !category.isEmpty()) {
            productPage = productService.findProductsByCategory(page, size, keyword, category);
        } else {
            productPage = productService.getAllProducts(page, size, keyword);
        }
        for (ProductDTO product : productPage) {
            List<ReviewDTO> productReviews = reviewService.getReviewByProductId(product.getProductId());
            product.setReviewCount(productReviews.size());

            double productAverageRating = productReviews.stream().mapToDouble(ReviewDTO::getRating).average().orElse(0.0);
            product.setAverageRating(productAverageRating);
        }
        model.addAttribute("productPage", productPage);
    }

    public void getAllProduct(int page, int size, String keyword, Model model, String category, String shoeSize) {
        Page<ProductDTO> productPage;
        if (category != null && !category.isEmpty()) {
            productPage = productService.findProductsByCategoryAndSize(page, size, keyword, category, shoeSize);
        } else {
            productPage = productService.getAllProducts(page, size, keyword, shoeSize);
        }
        model.addAttribute("productPage", productPage);
    }

    // New method for price filtering
    public void getAllProduct(int page, int size, String keyword, Model model, String category, BigDecimal minPrice, BigDecimal maxPrice) {
        Page<ProductDTO> productPage;
        if (category != null && !category.isEmpty()) {
            productPage = productService.findProductsByCategoryAndPriceRange(page, size, keyword, category, minPrice, maxPrice);
        } else {
            productPage = productService.findProductsByPriceRange(page, size, keyword, minPrice, maxPrice);
        }
        for (ProductDTO product : productPage) {
            List<ReviewDTO> productReviews = reviewService.getReviewByProductId(product.getProductId());
            product.setReviewCount(productReviews.size());

            double productAverageRating = productReviews.stream().mapToDouble(ReviewDTO::getRating).average().orElse(0.0);
            product.setAverageRating(productAverageRating);
        }
        model.addAttribute("productPage", productPage);
    }
}

