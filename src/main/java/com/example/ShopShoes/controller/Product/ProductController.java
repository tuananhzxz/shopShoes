package com.example.ShopShoes.controller.Product;

import java.io.IOException;
import com.example.ShopShoes.dto.CategoryDTO;
import com.example.ShopShoes.dto.ProductDTO;
import com.example.ShopShoes.service.CategoryService;
import com.example.ShopShoes.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/product")
public class ProductController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final ProductFunction productFunction;

    @GetMapping
    public String product(Model model,
                          @RequestParam(value = "keyword", required = false) String keyword,
                          @RequestParam(value = "page", defaultValue = "0") int page,
                          @RequestParam(value = "size", defaultValue = "10") int size) {
        productFunction.getAllProduct(page, size, keyword, model);
        return "product/index";
    }
    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("productDTO", new ProductDTO());
        List<CategoryDTO> listCate = categoryService.getAllCategories();
        model.addAttribute("listCate", listCate);
        return "product/add";
    }

    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute("productDTO") ProductDTO productDTO,
                             BindingResult result,
                             Model model,
                             @RequestParam("imageFile") MultipartFile imageFile) {
        if (result.hasErrors()) {
            handleValidationErrors(result, model);
            return "product/add";
        }

        try {
            String fileName = saveImage(imageFile);
            productDTO.setImage(fileName);

            productService.createProduct(productDTO);
            model.addAttribute("successMessage", "Product added successfully!");
            model.addAttribute("productDTO", new ProductDTO());
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Error saving product: " + e.getMessage());
        }

        return "product/add";
    }

    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable("id") Long productId, Model model) {
        productFunction.showProduct(productId, model, productService, categoryService);
        return "product/edit";
    }



    @PostMapping("/edit/{id}")
    public String editProduct(@PathVariable("id") Long productId,
                              @Valid @ModelAttribute("productDTO") ProductDTO productDTO,
                              BindingResult result,
                              Model model,
                              @RequestParam("imageFile") MultipartFile imageFile) {
        if (result.hasErrors()) {
            handleValidationErrors(result, model);
            return "product/edit";
        }

        try {
            if (!imageFile.isEmpty()) {
                String fileName = saveImage(imageFile);
                productDTO.setImage(fileName);
            } else {
                ProductDTO existingProduct = productService.getProductById(productId);
                productDTO.setImage(existingProduct.getImage());
            }

            productService.updateProduct(productId, productDTO);
            model.addAttribute("successMessage", "Product updated successfully!");
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Error updating product: " + e.getMessage());
        }

        return "product/edit";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long productId, Model model) {
        try {
            productService.deleteProduct(productId);
            model.addAttribute("successMessage", "Product deleted successfully!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error deleting product: " + e.getMessage());
        }
        return "redirect:/admin/product";
    }

    private String saveImage(MultipartFile imageFile) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
        String uploadDir = "src/main/resources/static/upload";

        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try {
            Files.copy(imageFile.getInputStream(), uploadPath.resolve(fileName));
        } catch (IOException e) {
            throw new IOException("Could not save image file: " + fileName, e);
        }

        return fileName;
    }

    private void handleValidationErrors(BindingResult result, Model model) {
        if (result.getFieldError("price") != null) {
            model.addAttribute("priceError", "Giá tiền phải là số.");
        }
        if (result.getFieldError("stock") != null) {
            model.addAttribute("stockError", "Kho phải là số.");
        }
        if (result.getFieldError("size") != null) {
            model.addAttribute("sizeError", "Kích thước phải là số.");
        }
    }
}
