package com.example.ShopShoes.service;

import com.example.ShopShoes.dto.CategoryDTO;
import com.example.ShopShoes.dto.CategoryWithCountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CategoryService {

    CategoryDTO getCategoryById(Long id);
    List<CategoryDTO> getAllCategories();
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO);
    void deleteCategory(Long categoryId);
    Page<CategoryDTO> getAllCategories(Pageable pageable);
    Page<CategoryDTO> searchCategories(String keyword, Pageable pageable);

    List<CategoryWithCountDTO> getCategoriesWithCount();
}
