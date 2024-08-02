package com.example.ShopShoes.repository;

import com.example.ShopShoes.dto.CategoryDTO;
import com.example.ShopShoes.dto.CategoryWithCountDTO;
import com.example.ShopShoes.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findById(long id);

    Boolean existsCategoriesById(long id);

    Boolean existsCategoriesByName(String name);

    Page<Category> findByNameContaining(String name, Pageable pageable);

    @Query("SELECT new com.example.ShopShoes.dto.CategoryWithCountDTO(c.id, c.name, COUNT(p)) " +
            "FROM Category c LEFT JOIN Product p ON c.id = p.category.id " +
            "GROUP BY c.id, c.name")
    List<CategoryWithCountDTO> findCategoriesWithCount();

}
