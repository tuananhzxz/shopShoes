package com.example.ShopShoes.controller.Back;

import com.example.ShopShoes.dto.*;
import com.example.ShopShoes.service.BlogService;
import com.example.ShopShoes.service.ProductService;
import com.example.ShopShoes.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/")
public class HomeController {

    private final ProductService productService;
    private final ReviewService reviewService;
    private final BlogService blogService;

    @GetMapping
    public String index(Model model, RegisterDTO registerDto, LoginDTO loginDto) {
        List<ProductDTO> products = productService.getAllProducts();
        products = ReviewCount(products, reviewService);
        if (products.size() > 6) {
            products = products.subList(0, 6);
        }
        List<ProductDTO> productMore = productService.getAllProducts();
        productMore = ReviewCount(productMore, reviewService);
        if (productMore.size() > 10) {
            productMore = productMore.subList(0, 10);
        }
        List<ProductDTO> nikeProducts = productService.getProductsByCategory("Nike");
        nikeProducts = ReviewCount(nikeProducts, reviewService);
        if (nikeProducts.size() > 6) {
            nikeProducts = products.subList(0, 6);
        }
        List<ProductDTO> audiProducts = productService.getProductsByCategory("Audi");
        audiProducts = ReviewCount(audiProducts, reviewService);
        if (audiProducts.size() > 6) {
            audiProducts = products.subList(0, 6);
        }
        List<BlogDTO> blogServiceList = blogService.getAllBlogs();
        if (blogServiceList.size() > 4) {
            blogServiceList = blogServiceList.subList(0, 4);
        }
        model.addAttribute("adidasProduct", productService.getProductsByCategory("Adidas").size());
        model.addAttribute("sneakerProduct", productService.getProductsByCategory("Sneaker").size());
        model.addAttribute("blogServiceList", blogServiceList);
        model.addAttribute("productMore", productMore);
        model.addAttribute("products", products);
        model.addAttribute("nikeProducts", nikeProducts);
        model.addAttribute("audiProducts", audiProducts);

        return "fe/index";
    }

    public static List<ProductDTO> ReviewCount(List<ProductDTO> products, ReviewService reviewService) {
        for (ProductDTO product : products) {
            List<ReviewDTO> productReviews = reviewService.getReviewByProductId(product.getProductId());
            product.setReviewCount(productReviews.size());

            double productAverageRating = productReviews.stream().mapToDouble(ReviewDTO::getRating).average().orElse(0.0);
            product.setAverageRating(productAverageRating);
        }
        return products;
    }

}


