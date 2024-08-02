package com.example.ShopShoes.service.Impl;

import com.example.ShopShoes.dto.BlogDTO;
import com.example.ShopShoes.entity.Blog;
import com.example.ShopShoes.repository.BlogRepository;
import com.example.ShopShoes.service.BlogService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final ModelMapper modelMapper;
    private final BlogRepository blogRepository;

    @Override
    public BlogDTO createBlog(BlogDTO blogDTO) {
        BlogDTO blog = modelMapper.map(blogDTO, BlogDTO.class);
        Blog savedBlog = blogRepository.save(modelMapper.map(blog, Blog.class));
        return modelMapper.map(savedBlog, BlogDTO.class);
    }

    @Override
    public BlogDTO updateBlog(Long id, BlogDTO blogDTO) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
        blog.setTitle(blogDTO.getTitle());
        blog.setContent(blogDTO.getContent());
        blog.setCategory(blogDTO.getCategory());
        blog.setCreatedAt(blogDTO.getCreatedAt());
        blog.setUpdatedAt(blogDTO.getUpdatedAt());
        Blog updatedBlog = blogRepository.save(blog);
        return modelMapper.map(updatedBlog, BlogDTO.class);
    }

    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public BlogDTO getBlogById(Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
        return modelMapper.map(blog, BlogDTO.class);
    }

    @Override
    public List<BlogDTO> getAllBlogs() {
        List<Blog> blogs = blogRepository.findAll();
        return blogs.stream()
                .map(blog -> modelMapper.map(blog, BlogDTO.class))
                .toList();
    }

    @Override
    public List<BlogDTO> getBlogsByCategory(Long categoryId) {
        return null;
    }

    @Override
    public Page<BlogDTO> getAllBlogsPage(Pageable pageable) {
        Page<Blog> blogPage = blogRepository.findAll(pageable);
        List<BlogDTO> blogDTOS = blogPage.getContent().stream()
                .map(blog -> modelMapper.map(blog, BlogDTO.class))
                .toList();
        return new PageImpl<>(blogDTOS, pageable, blogPage.getTotalElements());
    }

    @Override
    public Page<BlogDTO> searchBlog(String keyword, Pageable pageable) {
        Page<Blog> blogPage = blogRepository.searchBlog(keyword, pageable);
        List<BlogDTO> blogDTOS = blogPage.getContent().stream()
                .map(blog -> modelMapper.map(blog, BlogDTO.class))
                .toList();
        return new PageImpl<>(blogDTOS, pageable, blogPage.getTotalElements());
    }

    @Override
    public List<BlogDTO> getRandomRelatedPosts() {
        List<Blog> blogs = blogRepository.getRandomPost();
        return blogs.stream()
                .map(blog -> modelMapper.map(blog, BlogDTO.class))
                .toList();
    }

}
