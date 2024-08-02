package com.example.ShopShoes.service;

import com.example.ShopShoes.dto.CategoryDTO;
import com.example.ShopShoes.dto.ProductDTO;
import com.example.ShopShoes.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    ProductDTO getProductById(Long productId);
    List<ProductDTO> getAllProducts();
    ProductDTO createProduct(ProductDTO productDTO);
    ProductDTO updateProduct(Long productId, ProductDTO productDTO);
    void deleteProduct(Long productId);
    Page<ProductDTO> getAllProduct(Pageable pageable);
    Page<ProductDTO> searchProduct(String keyword, Pageable pageable);
    Page<ProductDTO> getProductsByCategory(Long categoryId, Pageable pageable);
    List<ProductDTO> getProductsByCategory(String category);
    List<String> getAllSizes(Long productId);
    List<String> getAllSizes();
    List<BigDecimal> getAllPrices();
    String getAllSizesAsString(Long productId);
    List<ProductDTO> getRandomProduct();
    List<ProductDTO> getProductByCategory(String category);
    Page<ProductDTO> getAllProducts(int page, int size, String keyword);
    Page<ProductDTO> findProductsByCategory(int page, int size, String keyword, String category);
    Page<ProductDTO> findProductsByCategoryAndSize(int page, int size, String keyword, String category, String shoeSize);
    Page<ProductDTO> getAllProducts(int page, int size, String keyword, String shoeSize);
    Page<ProductDTO> searchByNameOrCategory(String query, int page, int size);
    Page<ProductDTO> getAllProducts(int page, int size);
    Page<ProductDTO> findProductsByPriceRange(int page, int size, String keyword, BigDecimal minPrice, BigDecimal maxPrice);
    Page<ProductDTO> findProductsByCategoryAndPriceRange(int page, int size, String keyword, String category, BigDecimal minPrice, BigDecimal maxPrice);
    List<ProductDTO> getProductRecently();
}
