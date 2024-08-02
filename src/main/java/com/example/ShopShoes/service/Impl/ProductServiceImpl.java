package com.example.ShopShoes.service.Impl;

import com.example.ShopShoes.dto.ProductDTO;
import com.example.ShopShoes.entity.Category;
import com.example.ShopShoes.entity.Product;
import com.example.ShopShoes.repository.CategoryRepository;
import com.example.ShopShoes.repository.ProductRepository;
import com.example.ShopShoes.service.ProductService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductDTO getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);

        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existingProduct.setName(productDTO.getName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setSize(productDTO.getSize());
        existingProduct.setColor(productDTO.getColor());
        existingProduct.setStock(productDTO.getStock());
        existingProduct.setImage(productDTO.getImage());

        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        existingProduct.setCategory(category);

        Product updatedProduct = productRepository.save(existingProduct);
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    @Override
    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(productId);
    }

    @Override
    public Page<ProductDTO> getAllProduct(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        List<ProductDTO> productDTOs = productPage.getContent().stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        return new PageImpl<>(productDTOs, pageable, productPage.getTotalElements());
    }

    @Override
    public Page<ProductDTO> searchProduct(String keyword, Pageable pageable) {
        Page<Product> productPage = productRepository.findByNameContaining(keyword, pageable);
        List<ProductDTO> productDTOs = productPage.getContent().stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        return new PageImpl<>(productDTOs, pageable, productPage.getTotalElements());
    }

    @Override
    public Page<ProductDTO> getProductsByCategory(Long categoryId, Pageable pageable) {
        Page<Product> productPage = productRepository.findByCategoryId(categoryId, pageable);
        List<ProductDTO> productDTOs = productPage.getContent().stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        return new PageImpl<>(productDTOs, pageable, productPage.getTotalElements());
    }

    @Override
    public List<ProductDTO> getProductsByCategory(String category) {
        List<Product> products = productRepository.findByCategoryName(category);
        return products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllSizes(Long productId) {
        return productRepository.findSizesByProductId(productId);
    }

    @Override
    public List<String> getAllSizes() {
        return productRepository.findAllSizes();
    }

    @Override
    public List<BigDecimal> getAllPrices() {
        return productRepository.findAllPrices();
    }

    @Override
    public String getAllSizesAsString(Long productId) {
        return productRepository.findSizesAsStringByProductId(productId);
    }

    @Override
    public List<ProductDTO> getRandomProduct() {
        List<Product> products = productRepository.findRandomProduct();
        return products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getProductByCategory(String category) {
        List<Product> products = productRepository.getProductByCategory(category);
        return products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductDTO> getAllProducts(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        if (keyword == null || keyword.isEmpty()) {
            Page<Product> productPage = productRepository.findAll(pageable);
            List<ProductDTO> productDTOs = productPage.getContent().stream()
                    .map(product -> modelMapper.map(product, ProductDTO.class))
                    .toList();
            return new PageImpl<>(productDTOs, pageable, productPage.getTotalElements());
        } else {
            Page<Product> productPage = productRepository.findByNameContaining(keyword, pageable);
            List<ProductDTO> productDTOs = productPage.getContent().stream()
                    .map(product -> modelMapper.map(product, ProductDTO.class))
                    .toList();
            return new PageImpl<>(productDTOs, pageable, productPage.getTotalElements());
        }
    }

    @Override
    public Page<ProductDTO> findProductsByCategory(int page, int size, String keyword, String category) {
        Pageable pageable = PageRequest.of(page, size);
        if (keyword == null || keyword.isEmpty()) {
            Page<Product> productPage = productRepository.findByCategory(category, pageable);
            List<ProductDTO> productDTOs = productPage.getContent().stream()
                    .map(product -> modelMapper.map(product, ProductDTO.class))
                    .toList();
            return new PageImpl<>(productDTOs, pageable, productPage.getTotalElements());
        } else {
            Page<Product> productPage = productRepository.findByCategoryAndKeyword(category, keyword, pageable);
            List<ProductDTO> productDTOs = productPage.getContent().stream()
                    .map(product -> modelMapper.map(product, ProductDTO.class))
                    .toList();
            return new PageImpl<>(productDTOs, pageable, productPage.getTotalElements());
        }
    }

    @Override
    public Page<ProductDTO> findProductsByCategoryAndSize(int page, int size, String keyword, String category, String shoeSize) {
        Pageable pageable = PageRequest.of(page, size);
        if (keyword == null || keyword.isEmpty()) {
            Page<Product> productPage = productRepository.findByCategoryAndSize(category, shoeSize, pageable);
            List<ProductDTO> productDTOs = productPage.getContent().stream()
                    .map(product -> modelMapper.map(product, ProductDTO.class))
                    .toList();
            return new PageImpl<>(productDTOs, pageable, productPage.getTotalElements());
        } else {
            Page<Product> productPage = productRepository.findByCategoryAndKeywordAndSize(category, keyword, shoeSize, pageable);
            List<ProductDTO> productDTOs = productPage.getContent().stream()
                    .map(product -> modelMapper.map(product, ProductDTO.class))
                    .toList();
            return new PageImpl<>(productDTOs, pageable, productPage.getTotalElements());
        }
    }

    @Override
    public Page<ProductDTO> getAllProducts(int page, int size, String keyword, String shoeSize) {
        Pageable pageable = PageRequest.of(page, size);
        if (keyword == null || keyword.isEmpty()) {
            Page<Product> productPage = productRepository.findBySize(shoeSize, pageable);
            List<ProductDTO> productDTOs = productPage.getContent().stream()
                    .map(product -> modelMapper.map(product, ProductDTO.class))
                    .toList();
            return new PageImpl<>(productDTOs, pageable, productPage.getTotalElements());
        } else {
            Page<Product> productPage = productRepository.findByKeywordAndSize(keyword, shoeSize, pageable);
            List<ProductDTO> productDTOs = productPage.getContent().stream()
                    .map(product -> modelMapper.map(product, ProductDTO.class))
                    .toList();
            return new PageImpl<>(productDTOs, pageable, productPage.getTotalElements());
        }
    }

    @Override
    public Page<ProductDTO> searchByNameOrCategory(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findByNameContainingOrCategoryNameContaining(query, pageable);
        List<ProductDTO> productDTOs = productPage.getContent().stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        return new PageImpl<>(productDTOs, pageable, productPage.getTotalElements());
    }

    @Override
    public Page<ProductDTO> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findAll(pageable);
        List<ProductDTO> productDTOs = productPage.getContent().stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        return new PageImpl<>(productDTOs, pageable, productPage.getTotalElements());
    }

    @Override
    public Page<ProductDTO> findProductsByCategoryAndPriceRange(int page, int size, String keyword, String category, BigDecimal minPrice, BigDecimal maxPrice) {
        Pageable pageable = PageRequest.of(page, size);
        if (keyword == null || keyword.isEmpty()) {
            Page<Product> productDTOS = productRepository.findProductsByCategoryAndPriceRange(category, minPrice, maxPrice, pageable);
            return productDTOS.map(product -> modelMapper.map(product, ProductDTO.class));
        } else {
            Page<Product> products = productRepository.findProductsByCategoryAndKeywordAndPriceRange(category, keyword, minPrice, maxPrice, pageable);
            return products.map(product -> modelMapper.map(product, ProductDTO.class));
        }
    }

    @Override
    public Page<ProductDTO> findProductsByPriceRange(int page, int size, String keyword, BigDecimal minPrice, BigDecimal maxPrice) {
        Pageable pageable = PageRequest.of(page, size);
        if (keyword == null || keyword.isEmpty()) {
            Page<Product> productPage = productRepository.findProductsByPriceRange(minPrice, maxPrice, pageable);
            List<ProductDTO> productDTOs = productPage.getContent().stream()
                    .map(product -> modelMapper.map(product, ProductDTO.class))
                    .toList();
            return new PageImpl<>(productDTOs, pageable, productPage.getTotalElements());
        } else {
            Page<Product> products = productRepository.findProductsByKeywordAndPriceRange(keyword, minPrice, maxPrice, pageable);
            List<ProductDTO> productDTOs = products.getContent().stream()
                    .map(product -> modelMapper.map(product, ProductDTO.class))
                    .toList();
            return new PageImpl<>(productDTOs, pageable, products.getTotalElements());
        }
    }

    @Override
    public List<ProductDTO> getProductRecently() {
        List<Product> products = productRepository.findTop7ByOrderByCreatedAtDesc();
        return products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }

}
