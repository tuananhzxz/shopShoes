package com.example.ShopShoes.repository;

import com.example.ShopShoes.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {

    @Query("SELECT b FROM Blog b WHERE b.title LIKE %?1% OR b.content LIKE %?1%")
    Page<Blog> searchBlog(String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM blogs ORDER BY RAND() LIMIT 4", nativeQuery = true)
    List<Blog> getRandomPost();
}
