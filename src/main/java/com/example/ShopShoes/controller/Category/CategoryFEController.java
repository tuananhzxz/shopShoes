package com.example.ShopShoes.controller.Category;

import com.example.ShopShoes.controller.Product.ProductFunction;
import com.example.ShopShoes.dto.ProductDTO;
import com.example.ShopShoes.entity.Product;
import com.example.ShopShoes.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
    @Controller
    @AllArgsConstructor
    @RequestMapping("/category")
    public class CategoryFEController {

        private final CategoryFunction categoryFunction;
        private final ProductFunction productFunction;
        private final ProductService productService;

        @GetMapping
        public String category(Model model,
                               @RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "9") int size,
                               @RequestParam(value = "category", required = false) String category,
                               @RequestParam(value = "minPrice", defaultValue = "0") BigDecimal minPrice,
                               @RequestParam(value = "maxPrice", defaultValue = "10000") BigDecimal maxPrice) {

            categoryFunction.getAllCategory(page, size, model, keyword);
            productFunction.getAllProduct(page, size, keyword, model, category, minPrice, maxPrice);

            List<String> selectedCategories = (category != null && !category.isEmpty())
                    ? Arrays.asList(category.split(","))
                    : Collections.emptyList();

            model.addAttribute("selectedCategories", selectedCategories);
            model.addAttribute("categories", categoryFunction.getCategoriesWithCount());
            model.addAttribute("size", productService.getAllSizes());
            model.addAttribute("price", productService.getAllPrices());
            return "fe/category";
        }
    }


