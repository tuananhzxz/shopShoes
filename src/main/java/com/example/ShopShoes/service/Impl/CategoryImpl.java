package com.example.ShopShoes.service.Impl;

import com.example.ShopShoes.dto.CategoryDTO;
import com.example.ShopShoes.dto.CategoryWithCountDTO;
import com.example.ShopShoes.entity.Category;
import com.example.ShopShoes.repository.CategoryRepository;
import com.example.ShopShoes.service.CategoryService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryImpl implements CategoryService {

    private ModelMapper modelMapper;
    private CategoryRepository categoryRepository;

    @Override
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> category = categoryRepository.findAll();
        return category.stream().map(category1 -> modelMapper.map(category1, CategoryDTO.class)).toList();
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.existsCategoriesByName(categoryDTO.getName())) {
            throw new RuntimeException("Category already exists");
        }

        Category category = modelMapper.map(categoryDTO, Category.class);
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {
        Category existingCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));
        existingCategory.setName(categoryDTO.getName());
        existingCategory.setCategoryStatus(categoryDTO.getCategoryStatus());
        Category updatedCategory = categoryRepository.save(existingCategory);
        return modelMapper.map(updatedCategory, CategoryDTO.class);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public Page<CategoryDTO> getAllCategories(Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        List<CategoryDTO> categoryDTOs = categoryPage.getContent().stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();
        return new PageImpl<>(categoryDTOs, pageable, categoryPage.getTotalElements());
    }

    @Override
    public Page<CategoryDTO> searchCategories(String keyword, Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findByNameContaining(keyword, pageable);
        List<CategoryDTO> categoryDTOs = categoryPage.getContent().stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();
        return new PageImpl<>(categoryDTOs, pageable, categoryPage.getTotalElements());
    }
    @Override
    public List<CategoryWithCountDTO> getCategoriesWithCount() {
        // Fetch categories and counts from the repository or database
        return categoryRepository.findCategoriesWithCount();
    }
}
