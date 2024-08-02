package com.example.ShopShoes.controller.Category;

import com.example.ShopShoes.dto.CategoryDTO;
import com.example.ShopShoes.repository.CategoryRepository;
import com.example.ShopShoes.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    private final CategoryFunction categoryFunction;

    @GetMapping
    public String category(Model model,
                           @RequestParam(value = "keyword", required = false) String keyword,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "size", defaultValue = "10") int size) {
        categoryFunction.getAllCategory(page, size, model, keyword);
        return "category/index";
    }

    @GetMapping("/add")
    public String addCategory(Model model) {
        model.addAttribute("categoryDTO", new CategoryDTO());
        return "category/add";
    }

    @PostMapping("/add")
    public String addCategory(@Valid CategoryDTO categoryDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "category/add";
        }
        if (categoryRepository.existsCategoriesByName(categoryDTO.getName())) {
            model.addAttribute("errorMessage", "Category already exists!");
            return "category/add";
        }

        categoryService.createCategory(categoryDTO);
        model.addAttribute("successMessage", "Create category successful!");
        return "category/add";
    }

    @GetMapping("/edit/{id}")
    public String editCategory(@PathVariable("id") Long id, Model model) {
        CategoryDTO category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        return "category/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateCategory(@Valid @ModelAttribute("category") CategoryDTO category, @PathVariable("id") Long id, Model model, BindingResult result) {
        if (result.hasErrors()) {
            return "category/edit";
        }
        categoryService.updateCategory(id, category);
        model.addAttribute("successMessage", "Update category successful!");
        return "category/edit";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id, Model model) {
        categoryService.deleteCategory(id);
        model.addAttribute("successMessage", "Delete category successful!");
        return "redirect:/admin/category";
    }

}
