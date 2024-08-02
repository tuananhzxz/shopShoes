package com.example.ShopShoes.controller.Category;

import com.example.ShopShoes.controller.Back.HomeController;
import com.example.ShopShoes.dto.CategoryDTO;
import com.example.ShopShoes.dto.CategoryWithCountDTO;
import com.example.ShopShoes.service.CategoryService;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import lombok.AllArgsConstructor;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryFunction {

    private CategoryService categoryService;

    protected void getAllCategory(int page, int size, Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CategoryDTO> categoryPage = categoryService.getAllCategories(pageable);
        model.addAttribute("categoryPage", categoryPage);
    }

    protected void getAllCategory(int page, int size, Model model, String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CategoryDTO> categoryPage;
        if (keyword != null && !keyword.isEmpty()) {
            categoryPage = categoryService.searchCategories(keyword, pageable);
        } else {
            categoryPage = categoryService.getAllCategories(pageable);
        }
        model.addAttribute("categoryPage", categoryPage);
        model.addAttribute("keyword", keyword);
    }

    public List<CategoryWithCountDTO> getCategoriesWithCount() {
        return categoryService.getCategoriesWithCount();
    }

}
