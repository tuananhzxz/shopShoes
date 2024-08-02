package com.example.ShopShoes.controller.Blog;
import java.io.IOException;
import com.example.ShopShoes.dto.BlogDTO;
import com.example.ShopShoes.dto.CategoryDTO;
import com.example.ShopShoes.dto.ProductDTO;
import com.example.ShopShoes.service.BlogService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import javax.validation.Valid;

@Controller
@RequestMapping
@AllArgsConstructor
public class BlogController {

    private final BlogService blogService;

    @GetMapping("/admin/blog")
    public String adminBlog(Model model,
                            @RequestParam(value = "keyword", required = false) String keyword,
                            @RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (keyword == null) {
            model.addAttribute("blogPage", blogService.getAllBlogsPage(pageable));
        } else {
            model.addAttribute("blogPage", blogService.searchBlog(keyword, pageable));
        }
        model.addAttribute("keyword", keyword);
        return "blog/index";
    }

    @GetMapping("/admin/blog/add")
    public String showAddBlogForm(Model model) {
        model.addAttribute("blog", new BlogDTO());
        return "blog/add";
    }

    @PostMapping("/admin/blog/add")
    public String addBlogForm(@Valid @ModelAttribute("blogDTO") BlogDTO blogDTO,
                              BindingResult result,
                              Model model,
                              @RequestParam("imageFile") MultipartFile imageFile) {
        if (result.hasErrors()) {
            return "blog/add";
        }

        try {
            String fileName = saveImage(imageFile);
            blogDTO.setImage(fileName);
            blogService.createBlog(blogDTO);
            return "redirect:/admin/blog";
        } catch (IOException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/admin/blog";
    }

    @GetMapping("/admin/blog/edit/{id}")
    public String editBlog(@PathVariable Long id, Model model) {
        BlogDTO blogDTO = blogService.getBlogById(id);
        model.addAttribute("blogDTO", blogDTO);
        return "blog/edit";
    }

    @PostMapping("/admin/blog/edit/{id}")
    public String editBlogForm(@PathVariable Long id, Model model,@Valid @ModelAttribute BlogDTO blogDTO, BindingResult result, @RequestParam("imageFile") MultipartFile imageFile) {
        if (result.hasErrors()) {
            return "blog/edit";
        }

        try {
            if (!imageFile.isEmpty()) {
                String fileName = saveImage(imageFile);
                blogDTO.setImage(fileName);
            } else {
                BlogDTO existingBlog = blogService.getBlogById(id);
                blogDTO.setImage(existingBlog.getImage());
            }

            blogService.updateBlog(id, blogDTO);
            model.addAttribute("successMessage", "Product updated successfully!");
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Error updating product: " + e.getMessage());
        }
        return "blog/edit";
    }

    @GetMapping("/admin/blog/delete/{id}")
    public String deleteBlog(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return "redirect:/admin/blog";
    }

    @GetMapping("/single{id}")
    public String singleBlog(@PathVariable Long id, Model model) {
        BlogDTO blogDTO = blogService.getBlogById(id);
        model.addAttribute("blog", blogDTO);

        List<BlogDTO> relatedPosts = blogService.getRandomRelatedPosts();
        model.addAttribute("relatedPosts", relatedPosts);

        return "fe/single";
    }

    @GetMapping("/blog")
    public String blog(Model model,
                       @RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "size", defaultValue = "10") int size) {
        if (page <= 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, size);
        if (keyword == null) {
            model.addAttribute("blogPage", blogService.getAllBlogsPage(pageable));
        } else {
            model.addAttribute("blogPage", blogService.searchBlog(keyword, pageable));
        }
        List<BlogDTO> relatedPosts = blogService.getRandomRelatedPosts();
        model.addAttribute("relatedPosts", relatedPosts);
        model.addAttribute("keyword", keyword);
        return "fe/blog";
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
}
