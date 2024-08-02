package com.example.ShopShoes.controller.Product;

import com.example.ShopShoes.dto.ProductDTO;
import com.example.ShopShoes.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
@RequestMapping("search")
public class SearchController {

    private final ProductService productService;

    @GetMapping()
    public String search(@RequestParam(value = "query", required = false) String query,
                         @RequestParam(value = "page", defaultValue = "0") int page,
                         @RequestParam(value = "size", defaultValue = "10") int size,
                         Model model) {
        Page<ProductDTO> searchResults;
        if (query != null && !query.isEmpty()) {
            searchResults = productService.searchByNameOrCategory(query, page, size);
            model.addAttribute("keyword", query);
        } else {
            searchResults = productService.getAllProducts(page, size);
        }

        model.addAttribute("searchResults", searchResults);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", searchResults.getTotalPages());
        model.addAttribute("pageSize", size);
        return "fe/search";
    }
}
