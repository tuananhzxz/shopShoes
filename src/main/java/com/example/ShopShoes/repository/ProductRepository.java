package com.example.ShopShoes.repository;

import com.example.ShopShoes.dto.ProductDTO;
import com.example.ShopShoes.entity.Category;
import com.example.ShopShoes.entity.Product;
import com.example.ShopShoes.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByNameContaining(String keyword, Pageable pageable);

    Page<Product> findByCategoryId(Long category_id, Pageable pageable);
    @Query("SELECT DISTINCT p.size FROM Product p WHERE p.productId = :productId")
    List<String> findSizesByProductId(@Param("productId") Long productId);

    @Query("SELECT DISTINCT p.size FROM Product p")
    List<String> findAllSizes();

    @Query("SELECT DISTINCT p.price FROM Product p")
    List<BigDecimal> findAllPrices();

    @Query("SELECT p.size FROM Product p WHERE p.productId = :productId")
    String findSizesAsStringByProductId(@Param("productId") Long productId);

    @Query("SELECT p.category.name FROM Product p WHERE p.productId = :productId")
    List<String> findCategoriesAsStringByProductId(@Param("productId") Long productId);

    @Query(value = "SELECT * FROM products ORDER BY RAND() LIMIT 4", nativeQuery = true)
    List<Product> findRandomProduct();

    List<Product> findByCategoryName(String category);

    @Query("SELECT p FROM Product p WHERE p.category.name = :category ORDER BY RAND() LIMIT 4")
    List<Product> getProductByCategory(@Param("category") String category);

    @Query("SELECT p FROM Product p WHERE p.category.name = :category")
    Page<Product> findByCategory(String category, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.name = :category AND p.name LIKE %:keyword%")
    Page<Product> findByCategoryAndKeyword(String category, String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.name = :category AND p.size = :shoeSize")
    Page<Product> findByCategoryAndSize(String category, String shoeSize, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.name = :category AND p.name LIKE %:keyword% AND p.size = :shoeSize")
    Page<Product> findByCategoryAndKeywordAndSize(String category, String keyword, String shoeSize, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.size = :shoeSize")
    Page<Product> findBySize(String shoeSize, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% AND p.size = :shoeSize")
    Page<Product> findByKeywordAndSize(String keyword, String shoeSize, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:query% OR p.category.name LIKE %:query%")
    Page<Product> findByNameContainingOrCategoryNameContaining(@Param("query") String query, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.name = :category AND p.name LIKE %:keyword% AND p.price >= :minPrice AND p.price <= :maxPrice")
    Page<Product> findByCategoryAndKeywordAndPriceRange(String category, String keyword, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.price >= :minPrice AND p.price <= :maxPrice")
    Page<Product> findProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% AND p.price >= :minPrice AND p.price <= :maxPrice")
    Page<Product> findProductsByKeywordAndPriceRange(String keyword, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.name = :category AND p.price >= :minPrice AND p.price <= :maxPrice")
    Page<Product> findProductsByCategoryAndPriceRange(String category, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.name = :category AND p.name LIKE %:keyword% AND p.price >= :minPrice AND p.price <= :maxPrice")
    Page<Product> findProductsByCategoryAndKeywordAndPriceRange(String category, String keyword, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% AND p.category.name = :category AND p.price >= :minPrice AND p.price <= :maxPrice")
    Page<Product> findByKeywordAndCategoryAndPriceBetween(String keyword, String category, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% AND p.price >= :minPrice AND p.price <= :maxPrice")
    Page<Product> findByKeywordAndPriceBetween(String keyword, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.name = :category AND p.price >= :minPrice AND p.price <= :maxPrice")
    Page<Product> findByCategoryAndPriceBetween(String category, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.price >= :minPrice AND p.price <= :maxPrice")
    Page<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    @Query("SELECT p FROM Product p ORDER BY p.createdAt DESC , RAND() LIMIT 7")
    List<Product> findTop7ByOrderByCreatedAtDesc();
}

