package com.example.ShopShoes.service;

import com.example.ShopShoes.dto.BlogDTO;
import com.example.ShopShoes.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BlogService {

    BlogDTO createBlog(BlogDTO blogDTO);

    BlogDTO updateBlog(Long id,BlogDTO blogDTO);

    void deleteBlog(Long id);

    BlogDTO getBlogById(Long id);

    List<BlogDTO> getAllBlogs();

    List<BlogDTO> getBlogsByCategory(Long categoryId);

    Page<BlogDTO> getAllBlogsPage(Pageable pageable);

    Page<BlogDTO> searchBlog(String keyword, Pageable pageable);

    List<BlogDTO> getRandomRelatedPosts();
}
