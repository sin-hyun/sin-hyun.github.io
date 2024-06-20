package com.example.board.repository;

import com.example.board.entity.Post;
import com.example.board.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCategory(Category category);
    List<Post> findByCategoryId(Long categoryId);
}
